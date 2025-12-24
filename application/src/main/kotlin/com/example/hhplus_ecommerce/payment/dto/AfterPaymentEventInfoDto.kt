package com.example.hhplus_ecommerce.payment.dto

import com.example.hhplus_ecommerce.balance.dto.BalanceDto
import java.time.LocalDateTime

class AfterPaymentEventInfoDto(
	val userId: Long,
	val orderId: Long,
	val currentBalance: Int,
	val paymentDate: LocalDateTime
) {
	companion object {
		fun of(userId: Long, orderId: Long, balanceDto: BalanceDto, paymentDate: LocalDateTime): AfterPaymentEventInfoDto {
			return AfterPaymentEventInfoDto(
				userId = userId,
				orderId = orderId,
				currentBalance = balanceDto.amount,
				paymentDate = paymentDate
			)
		}
	}
}