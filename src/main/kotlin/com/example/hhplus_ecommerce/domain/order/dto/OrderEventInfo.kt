package com.example.hhplus_ecommerce.domain.order.dto

data class OrderEventInfo(
	val userId: Long,
	val orderItemInfos: List<OrderItemInfo>
) {
	companion object {
		fun of(userId: Long, orderItemInfos: List<OrderItemInfo>): OrderEventInfo {
			return OrderEventInfo(userId, orderItemInfos)
		}
	}
}