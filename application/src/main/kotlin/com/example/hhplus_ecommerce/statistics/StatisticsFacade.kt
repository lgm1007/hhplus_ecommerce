package com.example.hhplus_ecommerce.statistics

import com.example.hhplus_ecommerce.order.OrderService
import com.example.hhplus_ecommerce.product.ProductService
import com.example.hhplus_ecommerce.statistics.dto.OrderProductStatisticsResponseDto
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@Component
class StatisticsFacade(
	private val orderService: OrderService,
	private val productService: ProductService
) {
	@Cacheable(value = ["topOrderProductStatistics"], key = "#day.toString() + '_' + #limit.toString()", cacheManager = "redisCacheManager")
	fun getTopOrderProductStatistics(day: Int, limit: Int): List<OrderProductStatisticsResponseDto> {
		// 특정 일자 (day) 내 주문량이 가장 많은 Top (limit) 주문 정보 조회
		val orderItemStatisticsInfos = orderService.getAllOrderItemsTopMoreThanDay(day, limit)
		val productDetailIds = orderItemStatisticsInfos.map { it.productDetailId }

		// 주문 정보에서 상품 목록 조회
		val productStatisticsInfos = productService.getAllProductStatisticsInfos(productDetailIds)

		return OrderProductStatisticsResponseDto.listOf(productStatisticsInfos, orderItemStatisticsInfos)
	}
}