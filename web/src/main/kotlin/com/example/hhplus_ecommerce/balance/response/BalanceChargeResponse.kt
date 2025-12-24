package com.example.hhplus_ecommerce.balance.response

import com.example.hhplus_ecommerce.balance.dto.BalanceDto

data class BalanceChargeResponse(
	val userId: Long,
	val chargedAmount: Int,
	val currentBalance: Int
) {
	companion object {
		fun of(balanceDto: BalanceDto, chargedAmount: Int): BalanceChargeResponse {
			return BalanceChargeResponse(
				userId = balanceDto.userId,
				chargedAmount = chargedAmount,
				currentBalance = balanceDto.amount
			)
		}
	}
}