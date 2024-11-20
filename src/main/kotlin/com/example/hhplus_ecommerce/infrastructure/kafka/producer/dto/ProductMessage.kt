package com.example.hhplus_ecommerce.infrastructure.kafka.producer.dto

data class ProductMessage(
	val userId: Long,
	val productDetailId: Long,
	val orderQuantity: Int
) {
}