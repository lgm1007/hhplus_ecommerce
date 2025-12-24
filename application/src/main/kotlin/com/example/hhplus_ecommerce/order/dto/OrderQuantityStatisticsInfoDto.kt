package com.example.hhplus_ecommerce.order.dto

import com.example.hhplus_ecommerce.order.OrderQuantityStatistics

data class OrderQuantityStatisticsInfoDto(
	val productDetailId: Long,
	val sumQuantity: Long
) {
	companion object {
		fun from(quantityStatistics: OrderQuantityStatistics): OrderQuantityStatisticsInfoDto {
			return OrderQuantityStatisticsInfoDto(
				quantityStatistics.productDetailId,
				quantityStatistics.sumQuantity
			)
		}
	}
}