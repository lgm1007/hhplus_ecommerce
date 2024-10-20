package com.example.hhplus_ecommerce.domain.payment.dto

import com.example.hhplus_ecommerce.domain.balance.dto.BalanceDto
import java.time.LocalDateTime

class PaymentResultInfo(
	val paymentId: Long,
	val orderId: Long,
	val currentBalance: Int,
	val paymentDate: LocalDateTime
) {
	companion object {
		fun of(paymentDto: PaymentDto, balanceDto: BalanceDto, orderId: Long): PaymentResultInfo {
			return PaymentResultInfo(
				paymentDto.id,
				orderId,
				balanceDto.amount,
				paymentDto.createdDate
			)
		}
	}
}