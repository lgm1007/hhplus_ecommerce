package com.example.hhplus_ecommerce.payment.dto

import com.example.hhplus_ecommerce.balance.dto.BalanceDto
import java.time.LocalDateTime

data class PaymentResultInfoDto(
	val paymentId: Long? = null,
	val orderId: Long,
	val currentBalance: Int,
	val paymentDate: LocalDateTime? = null
) {
	companion object {
		fun of(paymentDto: PaymentDto, balanceDto: BalanceDto, orderId: Long): PaymentResultInfoDto {
			return PaymentResultInfoDto(
				paymentId = paymentDto.paymentId,
				orderId = orderId,
				currentBalance = balanceDto.amount,
				paymentDate = paymentDto.createdDate
			)
		}
	}
}