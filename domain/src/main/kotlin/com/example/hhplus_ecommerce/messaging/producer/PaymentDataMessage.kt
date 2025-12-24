package com.example.hhplus_ecommerce.messaging.producer

import java.time.LocalDateTime

data class PaymentDataMessage(
	val userId: Long,
	val orderId: Long,
	val currentBalance: Int,
	val paymentDate: LocalDateTime
)