package com.example.hhplus_ecommerce.statistics

import com.example.hhplus_ecommerce.order.OrderItemJpaRepository
import com.example.hhplus_ecommerce.order.OrderJpaRepository
import com.example.hhplus_ecommerce.order.OrderService
import com.example.hhplus_ecommerce.order.dto.OrderItemDto
import com.example.hhplus_ecommerce.product.ProductCategory
import com.example.hhplus_ecommerce.product.ProductDetailJpaRepository
import com.example.hhplus_ecommerce.product.ProductJpaRepository
import com.example.hhplus_ecommerce.product.ProductService
import com.example.hhplus_ecommerce.product.entity.ProductDetailEntity
import com.example.hhplus_ecommerce.product.entity.ProductEntity
import com.example.hhplus_ecommerce.share.exception.ErrorStatus
import com.example.hhplus_ecommerce.share.exception.NotFoundException
import mu.KotlinLogging
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

@SpringBootTest
class StatisticsFacadeIntegrationTest {
	@Autowired private lateinit var statisticsFacade: StatisticsFacade
	@Autowired private lateinit var orderService: OrderService
	@Autowired private lateinit var productService: ProductService
	@Autowired private lateinit var orderRepository: OrderJpaRepository
	@Autowired private lateinit var orderItemRepository: OrderItemJpaRepository
	@Autowired private lateinit var productRepository: ProductJpaRepository
	@Autowired private lateinit var productDetailRepository: ProductDetailJpaRepository
	private val logger = KotlinLogging.logger {}

	@BeforeEach
	fun clearDB() {
		orderRepository.deleteAll()
		orderItemRepository.deleteAll()
		productRepository.deleteAll()
		productDetailRepository.deleteAll()
	}

	@Test
	@DisplayName("통계성 상위 상품 조회 - 3일 간 상위 5개 상품 정보 조회하기 기능")
	fun getTopProductsStatistics() {
		givenOrderItems()

		val startTime = System.currentTimeMillis()

		val actual = statisticsFacade.getTopOrderProductStatistics(day = 3, limit = 5)

		val endTime = System.currentTimeMillis()
		logger.info("실행 시간: ${endTime - startTime} milliseconds")

		assertThat(actual.size).isEqualTo(5)
		assertThat(actual[0].name).isEqualTo("상품A")
		assertThat(actual[0].totalSold).isEqualTo(55)
		assertThat(actual[1].name).isEqualTo("상품B")
		assertThat(actual[1].totalSold).isEqualTo(40)
		assertThat(actual[2].name).isEqualTo("상품C")
		assertThat(actual[2].totalSold).isEqualTo(30)
		assertThat(actual[3].name).isEqualTo("상품D")
		assertThat(actual[3].totalSold).isEqualTo(20)
		assertThat(actual[4].name).isEqualTo("상품E")
		assertThat(actual[4].totalSold).isEqualTo(10)
	}

	private fun givenOrderItems() {
		val productId1 = productRepository.save(ProductEntity(name = "상품A", description = "상품A 설명")).id
		val productId2 = productRepository.save(ProductEntity(name = "상품B", description = "상품B 설명")).id
		val productId3 = productRepository.save(ProductEntity(name = "상품C", description = "상품C 설명")).id
		val productId4 = productRepository.save(ProductEntity(name = "상품D", description = "상품D 설명")).id
		val productId5 = productRepository.save(ProductEntity(name = "상품E", description = "상품E 설명")).id
		val productId6 = productRepository.save(ProductEntity(name = "상품F", description = "상품F 설명")).id

		val detailId1 = productDetailRepository.save(ProductDetailEntity(productId = productId1, price = 50000, stockQuantity = 100, productCategory = ProductCategory.CLOTHES)).id
		val detailId2 = productDetailRepository.save(ProductDetailEntity(productId = productId2, price = 30000, stockQuantity = 100, productCategory = ProductCategory.COSMETICS)).id
		val detailId3 = productDetailRepository.save(ProductDetailEntity(productId = productId3, price = 100000, stockQuantity = 100, productCategory = ProductCategory.ELECTRONICS)).id
		val detailId4 = productDetailRepository.save(ProductDetailEntity(productId = productId4, price = 1000, stockQuantity = 100, productCategory = ProductCategory.COSMETICS)).id
		val detailId5 = productDetailRepository.save(ProductDetailEntity(productId = productId5, price = 1000, stockQuantity = 100, productCategory = ProductCategory.COSMETICS)).id
		val detailId6 = productDetailRepository.save(ProductDetailEntity(productId = productId6, price = 2000, stockQuantity = 100, productCategory = ProductCategory.COSMETICS)).id

		val orderItemDtos = listOf(
			OrderItemDto(orderItemId = 0, orderId = 1L, productDetailId = detailId1, quantity = 50, price = 2500000, createdDate = LocalDateTime.now()),
			OrderItemDto(orderItemId = 0, orderId = 1L, productDetailId = detailId2, quantity = 40, price = 1200000, createdDate = LocalDateTime.now()),
			OrderItemDto(orderItemId = 0, orderId = 2L, productDetailId = detailId3, quantity = 30, price = 3000000, createdDate = LocalDateTime.now()),
			OrderItemDto(orderItemId = 0, orderId = 3L, productDetailId = detailId4, quantity = 20, price = 20000, createdDate = LocalDateTime.now()),
			OrderItemDto(orderItemId = 0, orderId = 4L, productDetailId = detailId5, quantity = 10, price = 10000, createdDate = LocalDateTime.now()),
			OrderItemDto(orderItemId = 0, orderId = 4L, productDetailId = detailId6, quantity = 5, price = 5000, createdDate = LocalDateTime.now()),
			OrderItemDto(orderItemId = 0, orderId = 5L, productDetailId = detailId1, quantity = 5, price = 250000, createdDate = LocalDateTime.now()),
		)
		orderService.insertAllOrderItems(orderItemDtos)
	}

	@Test
	@DisplayName("통계성 상위 상품 조회 - 상품 정보가 존재하지 않는 경우 예외케이스")
	fun getTopProductStatisticsNotFoundProduct() {
		givenOrderItemsNoProduct()

		assertThatThrownBy { statisticsFacade.getTopOrderProductStatistics(day = 3, limit = 5) }
			.isInstanceOf(NotFoundException::class.java)
			.extracting("errorStatus")
			.isEqualTo(ErrorStatus.NOT_FOUND_PRODUCT)
	}

	private fun givenOrderItemsNoProduct() {
		val orderItemDtos = listOf(
			OrderItemDto(orderItemId = 0, orderId = 1L, productDetailId = 1L, quantity = 50, price = 2500000, createdDate = LocalDateTime.now()),
			OrderItemDto(orderItemId = 0, orderId = 1L, productDetailId = 2L, quantity = 40, price = 1200000, createdDate = LocalDateTime.now()),
			OrderItemDto(orderItemId = 0, orderId = 2L, productDetailId = 3L, quantity = 30, price = 3000000, createdDate = LocalDateTime.now()),
			OrderItemDto(orderItemId = 0, orderId = 3L, productDetailId = 4L, quantity = 20, price = 20000, createdDate = LocalDateTime.now()),
			OrderItemDto(orderItemId = 0, orderId = 4L, productDetailId = 5L, quantity = 10, price = 10000, createdDate = LocalDateTime.now()),
			OrderItemDto(orderItemId = 0, orderId = 4L, productDetailId = 6L, quantity = 5, price = 5000, createdDate = LocalDateTime.now()),
			OrderItemDto(orderItemId = 0, orderId = 5L, productDetailId = 1L, quantity = 5, price = 250000, createdDate = LocalDateTime.now()),
		)
		orderService.insertAllOrderItems(orderItemDtos)
	}
}