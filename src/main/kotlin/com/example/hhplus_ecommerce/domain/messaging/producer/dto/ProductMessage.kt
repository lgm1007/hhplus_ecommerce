package com.example.hhplus_ecommerce.domain.messaging.producer.dto

data class ProductMessage(
	val userId: Long,
	val productDetailId: Long,
	val orderQuantity: Int
) {
}