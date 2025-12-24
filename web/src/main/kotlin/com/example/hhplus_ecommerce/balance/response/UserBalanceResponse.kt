package com.example.hhplus_ecommerce.balance.response

import com.example.hhplus_ecommerce.balance.dto.BalanceDto

data class UserBalanceResponse(
	val userId: Long,
	val currentBalance: Int
) {
	companion object {
		fun from(balanceDto: BalanceDto): UserBalanceResponse {
			return UserBalanceResponse(
				userId = balanceDto.userId,
				currentBalance = balanceDto.amount
			)
		}
	}
}