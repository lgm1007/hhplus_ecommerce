package com.example.hhplus_ecommerce.usecase.statistics

import com.example.hhplus_ecommerce.domain.share.exception.ErrorStatus
import com.example.hhplus_ecommerce.application.order.OrderService
import com.example.hhplus_ecommerce.domain.order.dto.OrderItemDto
import com.example.hhplus_ecommerce.domain.product.ProductCategory
import com.example.hhplus_ecommerce.application.product.ProductService
import com.example.hhplus_ecommerce.application.statistics.StatisticsFacade
import com.example.hhplus_ecommerce.domain.share.exception.NotFoundException
import com.example.hhplus_ecommerce.infrastructure.order.OrderItemJpaRepository
import com.example.hhplus_ecommerce.infrastructure.order.OrderJpaRepository
import com.example.hhplus_ecommerce.infrastructure.product.ProductDetailJpaRepository
import com.example.hhplus_ecommerce.infrastructure.product.ProductJpaRepository
import com.example.hhplus_ecommerce.infrastructure.product.entity.Product
import com.example.hhplus_ecommerce.infrastructure.product.entity.ProductDetail
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

		val actual = statisticsFacade.getTopOrderProductStatistics(3, 5)

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
		val productId1 = productRepository.save(Product("상품A", "상품A 설명")).id
		val productId2 = productRepository.save(Product("상품B", "상품B 설명")).id
		val productId3 = productRepository.save(Product("상품C", "상품C 설명")).id
		val productId4 = productRepository.save(Product("상품D", "상품D 설명")).id
		val productId5 = productRepository.save(Product("상품E", "상품E 설명")).id
		val productId6 = productRepository.save(Product("상품F", "상품F 설명")).id

		val detailId1 = productDetailRepository.save(ProductDetail(productId1, 50000, 100, ProductCategory.CLOTHES)).id
		val detailId2 = productDetailRepository.save(ProductDetail(productId2, 30000, 100, ProductCategory.COSMETICS)).id
		val detailId3 = productDetailRepository.save(ProductDetail(productId3, 100000, 100, ProductCategory.ELECTRONICS)).id
		val detailId4 = productDetailRepository.save(ProductDetail(productId4, 1000, 100, ProductCategory.COSMETICS)).id
		val detailId5 = productDetailRepository.save(ProductDetail(productId5, 1000, 100, ProductCategory.COSMETICS)).id
		val detailId6 = productDetailRepository.save(ProductDetail(productId6, 2000, 100, ProductCategory.COSMETICS)).id

		val orderItemDtos = listOf(
			OrderItemDto(0, 1L, detailId1, 50, 2500000, LocalDateTime.now()),
			OrderItemDto(0, 1L, detailId2, 40, 1200000, LocalDateTime.now()),
			OrderItemDto(0, 2L, detailId3, 30, 3000000, LocalDateTime.now()),
			OrderItemDto(0, 3L, detailId4, 20, 20000, LocalDateTime.now()),
			OrderItemDto(0, 4L, detailId5, 10, 10000, LocalDateTime.now()),
			OrderItemDto(0, 4L, detailId6, 5, 5000, LocalDateTime.now()),
			OrderItemDto(0, 5L, detailId1, 5, 250000, LocalDateTime.now()),
		)
		orderService.insertAllOrderItems(orderItemDtos)
	}

	@Test
	@DisplayName("통계성 상위 상품 조회 - 상품 정보가 존재하지 않는 경우 예외케이스")
	fun getTopProductStatisticsNotFoundProduct() {
		givenOrderItemsNoProduct()

		assertThatThrownBy { statisticsFacade.getTopOrderProductStatistics(3, 5) }
			.isInstanceOf(NotFoundException::class.java)
			.extracting("errorStatus")
			.isEqualTo(ErrorStatus.NOT_FOUND_PRODUCT)
	}

	private fun givenOrderItemsNoProduct() {
		val orderItemDtos = listOf(
			OrderItemDto(0, 1L, 1L, 50, 2500000, LocalDateTime.now()),
			OrderItemDto(0, 1L, 2L, 40, 1200000, LocalDateTime.now()),
			OrderItemDto(0, 2L, 3L, 30, 3000000, LocalDateTime.now()),
			OrderItemDto(0, 3L, 4L, 20, 20000, LocalDateTime.now()),
			OrderItemDto(0, 4L, 5L, 10, 10000, LocalDateTime.now()),
			OrderItemDto(0, 4L, 6L, 5, 5000, LocalDateTime.now()),
			OrderItemDto(0, 5L, 1L, 5, 250000, LocalDateTime.now()),
		)
		orderService.insertAllOrderItems(orderItemDtos)
	}
}