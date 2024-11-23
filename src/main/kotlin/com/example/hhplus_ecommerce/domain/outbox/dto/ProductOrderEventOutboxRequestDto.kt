package com.example.hhplus_ecommerce.domain.outbox.dto

data class ProductOrderEventOutboxRequestDto(
	val userId: Long,
	val productDetailId: Long,
	val orderQuantity: Int,
) {
}