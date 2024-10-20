package com.example.hhplus_ecommerce.api.balance.response

import com.example.hhplus_ecommerce.domain.balance.dto.BalanceDto

class BalanceChargeResponse(
	val userId: Long,
	val chargedAmount: Int,
	val currentBalance: Int
) {
	companion object {
		fun of(balanceDto: BalanceDto, chargedAmount: Int): BalanceChargeResponse {
			return BalanceChargeResponse(
				balanceDto.userId,
				chargedAmount,
				balanceDto.amount
			)
		}
	}
}