package com.example.hhplus_ecommerce.order

import java.time.LocalDateTime

data class Order(
	val orderId: Long? = null,
	val userId: Long,
	val orderDate: LocalDateTime,
	val totalPrice: Int,
	var orderStatus: OrderStatus,
	var createdDate: LocalDateTime? = null,
	var lastModifiedDate: LocalDateTime? = null
)
