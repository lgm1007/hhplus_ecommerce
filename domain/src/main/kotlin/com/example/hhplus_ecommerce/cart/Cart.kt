package com.example.hhplus_ecommerce.cart

import java.time.LocalDateTime

data class Cart(
	val cartId: Long? = null,
	val userId: Long,
	val productDetailId: Long,
	val quantity: Int,
	var createdDate: LocalDateTime? = null
)
