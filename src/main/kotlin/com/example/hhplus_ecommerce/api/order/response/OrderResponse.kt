package com.example.hhplus_ecommerce.api.order.response

import com.example.hhplus_ecommerce.domain.order.OrderStatus
import com.example.hhplus_ecommerce.domain.order.dto.OrderItemDetailInfo
import java.time.LocalDateTime

class OrderResponse(
	val orderId: Long,
	val userId: Long,
	val orderDate: LocalDateTime,
	val totalPrice: Int,
	val status: OrderStatus,
	val orderItems: List<OrderItemDetailInfo>
) {
}