package com.example.hhplus_ecommerce.infrastructure.message.producer.dto

data class ProductMessage(
	val productDetailId: Long,
	val orderQuantity: Int
) {
}