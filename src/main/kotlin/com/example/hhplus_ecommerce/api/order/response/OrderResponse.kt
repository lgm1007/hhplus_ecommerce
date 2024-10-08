package com.example.hhplus_ecommerce.api.order.response

import com.example.hhplus_ecommerce.domain.order.dto.OrderItemDetail
import java.time.LocalDate

class OrderResponse(
	val orderId: Long,
	val userId: Long,
	val orderDate: LocalDate,
	val totalPrice: Int,
	val orderItems: List<OrderItemDetail>
) {
}