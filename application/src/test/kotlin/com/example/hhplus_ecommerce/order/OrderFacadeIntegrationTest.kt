package com.example.hhplus_ecommerce.order

import com.example.hhplus_ecommerce.order.dto.OrderItemInfoDto
import com.example.hhplus_ecommerce.outbox.OutboxEventStatus
import com.example.hhplus_ecommerce.outbox.ProductOrderEventOutboxRepository
import com.example.hhplus_ecommerce.product.ProductCategory
import com.example.hhplus_ecommerce.product.ProductDetailJpaRepository
import com.example.hhplus_ecommerce.product.ProductJpaRepository
import com.example.hhplus_ecommerce.product.ProductService
import com.example.hhplus_ecommerce.product.entity.ProductDetailEntity
import com.example.hhplus_ecommerce.product.entity.ProductEntity
import com.example.hhplus_ecommerce.share.exception.BadRequestException
import mu.KotlinLogging
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

@SpringBootTest
@EmbeddedKafka(partitions = 3, brokerProperties = ["listeners=PLAINTEXT://localhost:9092"], ports = [9092])
class OrderFacadeIntegrationTest {
	@Autowired private lateinit var orderFacade: OrderFacade
	@Autowired private lateinit var productService: ProductService
	@Autowired private lateinit var productRepository: ProductJpaRepository
	@Autowired private lateinit var productDetailRepository: ProductDetailJpaRepository
	@Autowired private lateinit var orderRepository: OrderJpaRepository
	@Autowired private lateinit var orderItemRepository: OrderItemJpaRepository
	@Autowired private lateinit var productOrderEventOutboxRepository: ProductOrderEventOutboxRepository
	private val logger = KotlinLogging.logger {}

	@BeforeEach
	fun clearDB() {
		productRepository.deleteAll()
		productDetailRepository.deleteAll()
		orderRepository.deleteAll()
		orderItemRepository.deleteAll()
	}

	@Test
	@DisplayName("상품에 대한 주문 단건 요청")
	fun productOrderRequestOnce() {
		val productId = productRepository.save(ProductEntity(name = "상품 A", description = "A 상품")).id
		val productDetailId = productDetailRepository.save(ProductDetailEntity(productId, price = 1000, stockQuantity = 100, productCategory = ProductCategory.CLOTHES)).id

		val orderInfo = orderFacade.productOrder(userId = 1L, listOf(OrderItemInfoDto(productDetailId, quantity = 50)))
		val productInfo = productService.getProductInfoById(productId)

		assertThat(orderInfo.totalPrice).isEqualTo(50000)
		assertThat(orderInfo.orderItemDetailInfos.size).isEqualTo(1)
		assertThat(orderInfo.orderItemDetailInfos[0].quantity).isEqualTo(50)
		assertThat(productInfo.stockQuantity).isEqualTo(50)
	}

	@Test
	@DisplayName("상품에 대한 주문 단건 요청 Lettuce 사용하여 락 획득")
	fun productOrderRequestOnceWithLettuce() {
		val productId = productRepository.save(ProductEntity(name = "상품 A", description = "A 상품")).id
		val productDetailId = productDetailRepository.save(ProductDetailEntity(productId, price = 1000, stockQuantity = 100, productCategory = ProductCategory.CLOTHES)).id

		val orderInfo = orderFacade.productOrderWithLettuce(userId = 1L, listOf(OrderItemInfoDto(productDetailId, quantity = 50)))
		val productInfo = productService.getProductInfoById(productId)

		assertThat(orderInfo.totalPrice).isEqualTo(50000)
		assertThat(orderInfo.orderItemDetailInfos.size).isEqualTo(1)
		assertThat(orderInfo.orderItemDetailInfos[0].quantity).isEqualTo(50)
		assertThat(productInfo.stockQuantity).isEqualTo(50)
	}

	@Test
	@DisplayName("주문 - 한 상품에 대한 동시 주문 요청 동시성 제어 테스트")
	fun productOrderConcurrency() {
		// 재고 20개 존재하는 상품에 대해 30번 주문 요청
		// 예상 성공 카운트 20, 실패 카운트 10, 남은 재고량 0
		val productId = productRepository.save(ProductEntity(name = "상품 A", description = "A 상품")).id
		val productDetailId = productDetailRepository.save(ProductDetailEntity(productId, price = 1000, stockQuantity = 20, productCategory = ProductCategory.CLOTHES)).id

		val executor = Executors.newFixedThreadPool(30)
		val countDownLatch = CountDownLatch(30)
		val successCount = AtomicInteger(0) // 성공 카운트
		val failCount = AtomicInteger(0)    // 실패 카운트

		try {
			val startTime = System.currentTimeMillis()
			repeat(30) {
				executor.submit {
					try {
						orderFacade.productOrder(userId = 1L, listOf(OrderItemInfoDto(productDetailId, quantity = 1)))
						successCount.incrementAndGet()
					} catch (e: BadRequestException) {
						failCount.incrementAndGet()
					} finally {
						countDownLatch.countDown()
					}
				}
			}

			countDownLatch.await()

			val endTime = System.currentTimeMillis()
			logger.info("실행 시간: ${endTime - startTime} milliseconds")

			val actual = productService.getProductInfoById(productId)

			assertThat(actual.stockQuantity).isEqualTo(0)
			assertThat(successCount.get()).isEqualTo(20)
			assertThat(failCount.get()).isEqualTo(10)
		} finally {
			executor.shutdown()
		}
	}

	@Test
	@DisplayName("주문에 대한 동시 주문 요청 동시성 제어 테스트 Lettuce 사용하여 락 획득 기능 구현")
	fun productOrderConcurrencyWithLettuce() {
		// 재고 20개 존재하는 상품에 대해 30번 주문 요청
		// 예상 성공 카운트 20, 실패 카운트 10, 남은 재고량 0
		val productId = productRepository.save(ProductEntity(name = "상품 A", description = "A 상품")).id
		val productDetailId = productDetailRepository.save(ProductDetailEntity(productId, price = 1000, stockQuantity = 20, productCategory = ProductCategory.CLOTHES)).id

		val executor = Executors.newFixedThreadPool(30)
		val countDownLatch = CountDownLatch(30)
		val successCount = AtomicInteger(0) // 성공 카운트
		val failCount = AtomicInteger(0)    // 실패 카운트

		try {
			val startTime = System.currentTimeMillis()
			repeat(30) {
				executor.submit {
					try {
						orderFacade.productOrderWithLettuce(userId = 1L, listOf(OrderItemInfoDto(productDetailId, quantity = 1)))
						successCount.incrementAndGet()
					} catch (e: BadRequestException) {
						failCount.incrementAndGet()
					} finally {
						countDownLatch.countDown()
					}
				}
			}

			countDownLatch.await()

			val endTime = System.currentTimeMillis()
			logger.info("실행 시간: ${endTime - startTime} milliseconds")

			val actual = productService.getProductInfoById(productId)

			assertThat(actual.stockQuantity).isEqualTo(0)
			assertThat(successCount.get()).isEqualTo(20)
			assertThat(failCount.get()).isEqualTo(10)
		} finally {
			executor.shutdown()
		}
	}

	@Test
	@DisplayName("주문에 대한 동시 주문 요청 동시성 제어 테스트 Redisson 사용하여 락 획득 기능 구현")
	fun productOrderConcurrencyWithRedisson() {
		// 재고 20개 존재하는 상품에 대해 30번 주문 요청
		// 예상 성공 카운트 20, 실패 카운트 10, 남은 재고량 0
		val productId = productRepository.save(ProductEntity(name = "상품 A", description = "A 상품")).id
		val productDetailId = productDetailRepository.save(ProductDetailEntity(productId, price = 1000, stockQuantity = 20, productCategory = ProductCategory.CLOTHES)).id

		val executor = Executors.newFixedThreadPool(30)
		val countDownLatch = CountDownLatch(30)
		val successCount = AtomicInteger(0) // 성공 카운트
		val failCount = AtomicInteger(0)    // 실패 카운트

		try {
			val startTime = System.currentTimeMillis()
			repeat(30) {
				executor.submit {
					try {
						orderFacade.productOrderWithRedisson(userId = 1L, listOf(OrderItemInfoDto(productDetailId, quantity = 1)))
						successCount.incrementAndGet()
					} catch (e: BadRequestException) {
						failCount.incrementAndGet()
					} finally {
						countDownLatch.countDown()
					}
				}
			}

			countDownLatch.await()

			val endTime = System.currentTimeMillis()
			logger.info("실행 시간: ${endTime - startTime} milliseconds")

			val actual = productService.getProductInfoById(productId)

			assertThat(actual.stockQuantity).isEqualTo(0)
			assertThat(successCount.get()).isEqualTo(20)
			assertThat(failCount.get()).isEqualTo(10)
		} finally {
			executor.shutdown()
		}
	}

	@Test
	@DisplayName("주문에 대한 동시 주문 요청 Kafka 이벤트 발행 기능 테스트")
	fun productOrderConcurrencyWithKafka() {
		// 재고 20개 존재하는 상품에 대해 30번 주문 요청
		// 예상 남은 재고량 0
		val productId = productRepository.save(ProductEntity(name = "상품 A", description = "A 상품")).id
		val productDetailId = productDetailRepository.save(ProductDetailEntity(productId, price = 1000, stockQuantity = 20, productCategory = ProductCategory.CLOTHES)).id

		val executor = Executors.newFixedThreadPool(30)
		val countDownLatch = CountDownLatch(30)

		try {
			val startTime = System.currentTimeMillis()
			repeat(30) {
				executor.submit {
					try {
						orderFacade.productOrderWithKafka(userId = 1L, listOf(OrderItemInfoDto(productDetailId, quantity = 1)))
					} catch (e: Exception) {
						logger.info("예외 발생!")
					} finally {
						countDownLatch.countDown()
					}
				}
			}

			countDownLatch.await()

			val endTime = System.currentTimeMillis()
			logger.info("실행 시간: ${endTime - startTime} milliseconds")

			Thread.sleep(2000)

			val actual = productService.getProductInfoById(productId)

			assertThat(actual.stockQuantity).isEqualTo(0)
		} finally {
			executor.shutdown()
		}
	}

	@Test
	@DisplayName("주문 요청 정상 처리 후 outbox 메시지 상태가 COMPLETE 인지 확인한다")
	fun outboxStatusUpdateCompleteAfterOrder() {
		val userId = 1L
		val productId = productRepository.save(ProductEntity(name = "상품 A", description = "A 상품")).id
		val productDetailId = productDetailRepository.save(ProductDetailEntity(productId, price = 1000, stockQuantity = 100, productCategory = ProductCategory.CLOTHES)).id

		orderFacade.productOrderWithKafka(userId, listOf(OrderItemInfoDto(productDetailId, quantity = 50)))

		Thread.sleep(2000)

		val actual = productOrderEventOutboxRepository.getAllByUserIdAndProductDetailId(userId, productDetailId)
		actual.forEach {
			assertThat(it.eventStatus).isEqualTo(OutboxEventStatus.COMPLETE)
		}
	}
}