package com.example.hhplus_ecommerce.api.payment.response

import java.time.LocalDateTime

class PaymentResponse(
	val paymentId: Long,
	val orderId: Long,
	val currentBalance: Int,
	val paymentDate: LocalDateTime
) {
}