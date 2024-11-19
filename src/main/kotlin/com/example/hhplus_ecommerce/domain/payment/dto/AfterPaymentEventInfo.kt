package com.example.hhplus_ecommerce.domain.payment.dto

import com.example.hhplus_ecommerce.domain.balance.dto.BalanceDto
import java.time.LocalDateTime

class AfterPaymentEventInfo(
	val userId: Long,
	val orderId: Long,
	val currentBalance: Int,
	val paymentDate: LocalDateTime
) {
	companion object {
		fun of(userId: Long, orderId: Long, balanceDto: BalanceDto, paymentDate: LocalDateTime): AfterPaymentEventInfo {
			return AfterPaymentEventInfo(
				userId = userId,
				orderId = orderId,
				currentBalance = balanceDto.amount,
				paymentDate = paymentDate
			)
		}
	}
}