package com.example.hhplus_ecommerce.domain.messaging.producer.dto

import com.example.hhplus_ecommerce.domain.payment.dto.AfterPaymentEventInfo
import java.time.LocalDateTime

data class PaymentDataMessage(
	val userId: Long,
	val orderId: Long,
	val currentBalance: Int,
	val paymentDate: LocalDateTime
) {
	companion object {
		fun from(paymentInfo: AfterPaymentEventInfo): PaymentDataMessage {
			return PaymentDataMessage(
				paymentInfo.userId,
				paymentInfo.orderId,
				paymentInfo.currentBalance,
				paymentInfo.paymentDate
			)
		}
	}
}