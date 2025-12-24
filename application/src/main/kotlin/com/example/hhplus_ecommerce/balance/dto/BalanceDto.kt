package com.example.hhplus_ecommerce.balance.dto

import com.example.hhplus_ecommerce.balance.Balance
import java.time.LocalDateTime

class BalanceDto(
	val balanceId: Long? = null,
	val userId: Long,
	val amount: Int,
	val createdDate: LocalDateTime? = null,
	val lastModifiedDate: LocalDateTime? = null
) {

	companion object {
		fun from(balance: Balance): BalanceDto {
			return BalanceDto(
				balanceId = balance.balanceId,
				userId = balance.userId,
				amount = balance.amount,
				createdDate = balance.createdDate,
				lastModifiedDate = balance.lastModifiedDate
			)
		}
	}
}