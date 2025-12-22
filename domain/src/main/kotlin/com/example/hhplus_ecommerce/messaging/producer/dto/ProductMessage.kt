package com.example.hhplus_ecommerce.messaging.producer.dto

data class ProductMessage(
	val userId: Long,
	val productDetailId: Long,
	val orderQuantity: Int
) {
}