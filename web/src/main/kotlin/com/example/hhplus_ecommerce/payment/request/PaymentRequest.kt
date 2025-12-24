package com.example.hhplus_ecommerce.payment.request

data class PaymentRequest(
	val userId: Long,
	val orderId: Long
) {
}