package com.example.hhplus_ecommerce.api.order.request

class OrderRequest(
	val userId: Long,
	val orderItems: List<OrderItem>
) {
}