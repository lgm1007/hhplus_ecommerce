package com.example.hhplus_ecommerce.api.order.request

import com.example.hhplus_ecommerce.domain.order.dto.OrderItem

class OrderRequest(
	val userId: Long,
	val orderItems: List<OrderItem>
) {
}