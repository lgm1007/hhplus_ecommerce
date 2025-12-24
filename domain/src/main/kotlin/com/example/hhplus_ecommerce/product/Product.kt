package com.example.hhplus_ecommerce.product

import java.time.LocalDateTime

data class Product(
	val productId: Long? = null,
	val name: String,
	val description: String,
	var createdDate: LocalDateTime? = null
)
