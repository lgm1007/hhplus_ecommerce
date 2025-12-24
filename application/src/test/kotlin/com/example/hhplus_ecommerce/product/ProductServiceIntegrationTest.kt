package com.example.hhplus_ecommerce.product

import com.example.hhplus_ecommerce.product.entity.ProductDetailEntity
import com.example.hhplus_ecommerce.product.entity.ProductEntity
import com.example.hhplus_ecommerce.share.exception.BadRequestException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

@SpringBootTest
class ProductServiceIntegrationTest {
	@Autowired private lateinit var productService: ProductService
	@Autowired private lateinit var productRepository: ProductJpaRepository
	@Autowired private lateinit var productDetailRepository: ProductDetailJpaRepository
	private val logger = KotlinLogging.logger {}

	@BeforeEach
	fun clearDB() {
		productRepository.deleteAll()
		productDetailRepository.deleteAll()
	}

	@Test
	@DisplayName("300000개의 데이터에서 상품 ID가 50000,100000,150000,200000,250000에 해당하는 상품 세부 정보 조회")
	fun getAllProductDetailByProductIds() {
		givenProductDetails(300000)

		val startTime = System.currentTimeMillis()

		val actual = productDetailRepository.findAllByProductIdIn(
			listOf(50000L, 100000L, 150000L, 200000L, 250000L)
		)

		val endTime = System.currentTimeMillis()
		logger.info("실행 시간: ${endTime - startTime} milliseconds")

		assertThat(actual.size).isEqualTo(5)
	}

	private fun givenProductDetails(size: Int) {
		for (i in 1..size) {
			productDetailRepository.save(ProductDetailEntity(i.toLong(), 1234, 500, ProductCategory.CLOTHES))
		}
	}

	@Test
	@DisplayName("상품 목록 조회 - 페이징하여 상품 목록 조회")
	fun getAllProductsWithPaging() {
		givenProducts(5)

		val actual = productService.getAllProductInfosWithPaging(page = 0, itemSize = 3)

		assertThat(actual.size).isEqualTo(3)
	}

	private fun givenProducts(size: Int) {
		for (i in 1..size) {
			val productId = productRepository.save(ProductEntity("상품${i}", "{i}번 상품")).id!!
			productDetailRepository.save(ProductDetailEntity(productId, 1000, 100, ProductCategory.CLOTHES))
		}
	}

	@Test
	@DisplayName("특정 상품 조회하기")
	fun getProductInfo() {
		val productId = productRepository.save(ProductEntity("상품 A", "A 상품")).id!!
		productDetailRepository.save(ProductDetailEntity(productId, 1000, 100, ProductCategory.CLOTHES))

		val startTime = System.currentTimeMillis()

		val actual = productService.getProductInfoById(productId)

		val endTime = System.currentTimeMillis()
		logger.info("실행 시간: ${endTime - startTime} milliseconds")

		assertThat(actual.price).isEqualTo(1000)
		assertThat(actual.stockQuantity).isEqualTo(100)
		assertThat(actual.name).isEqualTo("상품 A")
	}

	@Test
	@DisplayName("상품 재고 차감 - 동시에 재고 차감 시 동시성 제어 테스트")
	fun quantityDecreaseConcurrency() {
		// 상품 재고 3개에 대해 5번 동시 차감 요청 시
		// 예상 성공 카운트 3, 실패 카운트 2, 남은 재고양 0
		val productId = productRepository.save(ProductEntity(name = "상품 A", description = "A 상품")).id!!
		val detailId = productDetailRepository.save(ProductDetailEntity(productId, price = 1000, stockQuantity = 3, productCategory = ProductCategory.CLOTHES)).id!!

		val executor = Executors.newFixedThreadPool(5)
		val countDownLatch = CountDownLatch(5)
		val successCount = AtomicInteger(0) // 성공 카운트
		val failCount = AtomicInteger(0)    // 실패 카운트

		try {
			repeat(5) {
				executor.submit {
					try {
						productService.updateProductQuantityDecreaseWithDBLock(detailId, 1)
						successCount.incrementAndGet()
					} catch (e: BadRequestException) {
						failCount.incrementAndGet()
					} finally {
						countDownLatch.countDown()
					}
				}
			}

			countDownLatch.await()

			val actual = productService.getProductInfoById(productId)

			assertThat(actual.stockQuantity).isEqualTo(0)
			assertThat(successCount.get()).isEqualTo(3)
			assertThat(failCount.get()).isEqualTo(2)
		} finally {
			executor.shutdown()
		}
	}

	@Test
	@DisplayName("상품 재고 차감 - Coroutine 동시에 재고 차감 동시성 제어 테스트")
	fun quantityDecreaseConcurrencyWithCoroutine() {
		// 상품 재고 3개에 대해 5번 동시 차감 요청 시
		// 예상 성공 카운트 3, 실패 카운트 2, 남은 재고양 0
		val productId = productRepository.save(ProductEntity(name = "상품 A", description = "A 상품")).id!!
		val productDetailId = productDetailRepository.save(ProductDetailEntity(productId, price = 1000, stockQuantity = 3, productCategory = ProductCategory.CLOTHES)).id!!

		val successCount = AtomicInteger(0) // 성공 카운트
		val failCount = AtomicInteger(0)    // 실패 카운트

		runBlocking {
			repeat(5) {
				// launch: 새로운 Coroutine 생성하여 병렬적 실행
				// launch 로 각각의 반복 재고 차감 요청을 개별 Coroutine 에서 실행
				launch(Dispatchers.Default) {   // Dispatchers.Default = 멀티 스레드에서 실행되도록
					try {
						productService.updateProductQuantityDecreaseWithDBLock(productDetailId, orderQuantity = 1)
						successCount.incrementAndGet()
					} catch (e: BadRequestException) {
						failCount.incrementAndGet()
					}
				}
			}
		}

		val actual = productService.getProductInfoById(productId)

		assertThat(actual.stockQuantity).isEqualTo(0)
		assertThat(successCount.get()).isEqualTo(3)
		assertThat(failCount.get()).isEqualTo(2)
	}
}