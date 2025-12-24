package com.example.hhplus_ecommerce.statistics

import com.example.hhplus_ecommerce.order.OrderService
import com.example.hhplus_ecommerce.order.dto.OrderQuantityStatisticsInfoDto
import com.example.hhplus_ecommerce.product.ProductService
import com.example.hhplus_ecommerce.product.dto.ProductStatisticsInfoDto
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any

@ExtendWith(MockitoExtension::class)
class StatisticsFacadeUnitTest {
	@Mock
	lateinit var orderService: OrderService

	@Mock
	lateinit var productService: ProductService

	@InjectMocks
	lateinit var statisticsFacade: StatisticsFacade

	@Test
	@DisplayName("상위 주문 상품 통계 목록 조회하는 기능 단위테스트")
	fun getTopOrderProductStatistics() {
		`when`(orderService.getAllOrderItemsTopMoreThanDay(any(), any()))
			.thenReturn(listOf(
				OrderQuantityStatisticsInfoDto(productDetailId = 1L, sumQuantity = 100),
				OrderQuantityStatisticsInfoDto(productDetailId = 2L, sumQuantity = 90),
				OrderQuantityStatisticsInfoDto(productDetailId = 3L, sumQuantity = 80),
				OrderQuantityStatisticsInfoDto(productDetailId = 4L, sumQuantity = 70),
				OrderQuantityStatisticsInfoDto(productDetailId = 5L, sumQuantity = 60)
			))

		`when`(productService.getAllProductStatisticsInfos(any()))
			.thenReturn(listOf(
				ProductStatisticsInfoDto(productId = 1L, productDetailId = 1L, name = "상품 A"),
				ProductStatisticsInfoDto(productId = 2L, productDetailId = 2L, name = "상품 B"),
				ProductStatisticsInfoDto(productId = 3L, productDetailId = 3L, name = "상품 C"),
				ProductStatisticsInfoDto(productId = 4L, productDetailId = 4L, name = "상품 D"),
				ProductStatisticsInfoDto(productId = 5L, productDetailId = 5L, name = "상품 E")
			))

		val actual = statisticsFacade.getTopOrderProductStatistics(day = 3, limit = 5)

		assertThat(actual.size).isEqualTo(5)
		assertThat(actual[0].totalSold).isEqualTo(100)
		assertThat(actual[0].name).isEqualTo("상품 A")
		assertThat(actual[1].totalSold).isEqualTo(90)
		assertThat(actual[1].name).isEqualTo("상품 B")
		assertThat(actual[2].totalSold).isEqualTo(80)
		assertThat(actual[2].name).isEqualTo("상품 C")
		assertThat(actual[3].totalSold).isEqualTo(70)
		assertThat(actual[3].name).isEqualTo("상품 D")
		assertThat(actual[4].totalSold).isEqualTo(60)
		assertThat(actual[4].name).isEqualTo("상품 E")
	}
}