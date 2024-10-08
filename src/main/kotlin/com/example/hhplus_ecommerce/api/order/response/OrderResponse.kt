package com.example.hhplus_ecommerce.api.order.response

import java.time.LocalDate

class OrderResponse(
	val orderId: Long,
	val userId: Long,
	val orderDate: LocalDate,
	val totalPrice: Int,
	val orderItems: List<OrderItem>
) {
}