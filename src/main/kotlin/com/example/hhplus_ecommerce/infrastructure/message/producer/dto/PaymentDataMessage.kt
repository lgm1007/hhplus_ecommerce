package com.example.hhplus_ecommerce.infrastructure.message.producer.dto

import com.example.hhplus_ecommerce.domain.payment.dto.PaymentResultInfo
import java.time.LocalDateTime

data class PaymentDataMessage(
	val paymentId: Long,
	val orderId: Long,
	val currentBalance: Int,
	val paymentDate: LocalDateTime
) {
	companion object {
		fun from(paymentResultInfo: PaymentResultInfo): PaymentDataMessage {
			return PaymentDataMessage(
				paymentResultInfo.paymentId,
				paymentResultInfo.orderId,
				paymentResultInfo.currentBalance,
				paymentResultInfo.paymentDate
			)
		}
	}
}