package com.example.hhplus_ecommerce.usecase.statistics

import com.example.hhplus_ecommerce.domain.order.OrderService
import com.example.hhplus_ecommerce.domain.product.ProductService
import com.example.hhplus_ecommerce.domain.statistics.dto.OrderProductStatisticsResponseItem
import org.springframework.stereotype.Component

@Component
class StatisticsFacade(
	private val orderService: OrderService,
	private val productService: ProductService
) {
	fun getTopOrderProductStatistics(day: Int, limit: Int): List<OrderProductStatisticsResponseItem> {
		// 특정 일자 (day) 내 주문량이 가장 많은 Top (limit) 주문 정보 조회
		val orderItemStatisticsInfos = orderService.getAllOrderItemsTopMoreThanDay(day, limit)
		val productDetailIds = orderItemStatisticsInfos.map { it.productDetailId }

		// 주문 정보에서 상품 목록 조회
		val productStatisticsInfos = productService.getAllProductStatisticsInfos(productDetailIds)

		return OrderProductStatisticsResponseItem.listOf(productStatisticsInfos, orderItemStatisticsInfos)
	}
}