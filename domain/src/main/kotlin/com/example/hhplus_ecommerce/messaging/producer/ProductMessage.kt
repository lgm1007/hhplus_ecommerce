package com.example.hhplus_ecommerce.messaging.producer

data class ProductMessage(
	val userId: Long,
	val productDetailId: Long,
	val orderQuantity: Int
) {
}