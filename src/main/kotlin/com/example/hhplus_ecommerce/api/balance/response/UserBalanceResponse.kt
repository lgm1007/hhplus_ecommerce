package com.example.hhplus_ecommerce.api.balance.response

import com.example.hhplus_ecommerce.domain.balance.dto.BalanceDto

class UserBalanceResponse(
	val userId: Long,
	val currentBalance: Int
) {
	companion object {
		fun from(balanceDto: BalanceDto): UserBalanceResponse {
			return UserBalanceResponse(balanceDto.userId, balanceDto.amount)
		}
	}
}