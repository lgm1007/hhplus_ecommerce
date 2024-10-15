package com.example.hhplus_ecommerce.domain.payment.dto

import com.example.hhplus_ecommerce.domain.payment.PaymentStatus
import com.example.hhplus_ecommerce.infrastructure.payment.entity.Payment
import java.time.LocalDateTime

class PaymentDto(
	val id: Long,
	val userId: Long,
	val orderId: Long,
	val price: Int,
	var paymentStatus: PaymentStatus,
	val createdDate: LocalDateTime
) {
	companion object {
		fun from(payment: Payment): PaymentDto {
			return PaymentDto(
				payment.id,
				payment.userId,
				payment.orderId,
				payment.price,
				payment.paymentStatus,
				payment.createdDate
			)
		}
	}
}