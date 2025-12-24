package com.example.hhplus_ecommerce.payment.response

import com.example.hhplus_ecommerce.payment.dto.PaymentResultInfoDto
import java.time.LocalDateTime

data class PaymentResponse(
	val paymentId: Long,
	val orderId: Long,
	val currentBalance: Int,
	val paymentDate: LocalDateTime
) {
	companion object {
		fun from(paymentResultInfoDto: PaymentResultInfoDto): PaymentResponse {
			return PaymentResponse(
				paymentId = paymentResultInfoDto.paymentId!!,
				orderId = paymentResultInfoDto.orderId,
				currentBalance = paymentResultInfoDto.currentBalance,
				paymentDate = paymentResultInfoDto.paymentDate!!
			)
		}
	}
}