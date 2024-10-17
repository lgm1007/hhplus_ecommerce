package com.example.hhplus_ecommerce.usecase.statistics

import com.example.hhplus_ecommerce.domain.order.OrderService
import com.example.hhplus_ecommerce.domain.order.dto.OrderQuantityStatisticsInfo
import com.example.hhplus_ecommerce.domain.product.ProductService
import com.example.hhplus_ecommerce.domain.product.dto.ProductStatisticsInfo
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Assertions.*
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
				OrderQuantityStatisticsInfo(1L, 100),
				OrderQuantityStatisticsInfo(2L, 90),
				OrderQuantityStatisticsInfo(3L, 80),
				OrderQuantityStatisticsInfo(4L, 70),
				OrderQuantityStatisticsInfo(5L, 60),
			))

		`when`(productService.getAllProductStatisticsInfos(any()))
			.thenReturn(listOf(
				ProductStatisticsInfo(1L, 1L, "상품 A"),
				ProductStatisticsInfo(2L, 2L, "상품 B"),
				ProductStatisticsInfo(3L, 3L, "상품 C"),
				ProductStatisticsInfo(4L, 4L, "상품 D"),
				ProductStatisticsInfo(5L, 5L, "상품 E"),
			))

		val actual = statisticsFacade.getTopOrderProductStatistics(3, 5)

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