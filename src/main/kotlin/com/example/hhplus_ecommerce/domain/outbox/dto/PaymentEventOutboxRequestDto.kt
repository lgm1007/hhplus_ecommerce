package com.example.hhplus_ecommerce.domain.outbox.dto

data class PaymentEventOutboxRequestDto(
	val userId: Long,
	val orderId: Long
) {
}