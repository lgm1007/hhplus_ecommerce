package com.example.hhplus_ecommerce.domain.balance.dto

import com.example.hhplus_ecommerce.infrastructure.balance.entity.Balance
import java.time.LocalDateTime

class BalanceDto(
	val balanceId: Long,
	val userId: Long,
	val amount: Int,
	val createdDate: LocalDateTime,
	val lastModifiedDate: LocalDateTime
) {

	companion object {
		fun from(balance: Balance): BalanceDto {
			return BalanceDto(
				balance.id,
				balance.userId,
				balance.amount,
				balance.createdDate,
				balance.lastModifiedDate
			)
		}
	}
}