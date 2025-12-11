package com.example.hhplus_ecommerce.web.payment.response

import com.example.hhplus_ecommerce.domain.payment.dto.PaymentResultInfo
import java.time.LocalDateTime

class PaymentResponse(
	val paymentId: Long,
	val orderId: Long,
	val currentBalance: Int,
	val paymentDate: LocalDateTime
) {
	companion object {
		fun from(paymentResultInfo: PaymentResultInfo): PaymentResponse {
			return PaymentResponse(
				paymentResultInfo.paymentId,
				paymentResultInfo.orderId,
				paymentResultInfo.currentBalance,
				paymentResultInfo.paymentDate
			)
		}
	}
}