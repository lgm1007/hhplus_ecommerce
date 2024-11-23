package com.example.hhplus_ecommerce.domain.payment.dto

data class PaymentEventRequestInfo(
	val userId: Long,
	val orderId: Long
) {
}