package com.example.hhplus_ecommerce.payment

import java.time.LocalDateTime

data class Payment(
	val paymentId: Long? = null,
	val userId: Long,
	val orderId: Long,
	val price: Int,
	val paymentStatus: PaymentStatus,
	var createdDate: LocalDateTime? = null
)