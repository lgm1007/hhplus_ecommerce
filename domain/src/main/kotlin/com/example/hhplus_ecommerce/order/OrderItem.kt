package com.example.hhplus_ecommerce.order

import java.time.LocalDateTime

data class OrderItem(
	val orderItemId: Long? = null,
	val orderId: Long,
	val productDetailId: Long,
	val quantity: Int,
	val price: Int,
	var createdDate: LocalDateTime? = null
)
