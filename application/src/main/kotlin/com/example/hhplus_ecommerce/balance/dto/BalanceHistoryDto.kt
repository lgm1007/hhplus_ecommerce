package com.example.hhplus_ecommerce.balance.dto

import com.example.hhplus_ecommerce.infrastructure.balance.entity.BalanceHistoryEntity
import java.time.LocalDateTime

class BalanceHistoryDto(
	val id: Long,
	val balanceId: Long,
	val userId: Long,
	val updateAmount: Int,
	val createdDate: LocalDateTime
) {
	companion object {
		fun from(balanceHistory: BalanceHistoryEntity): BalanceHistoryDto {
			return BalanceHistoryDto(
				balanceHistory.id,
				balanceHistory.balanceId,
				balanceHistory.userId,
				balanceHistory.updateAmount,
				balanceHistory.createdDate
			)
		}

		fun of(balanceId: Long, userId: Long, updateAmount: Int): BalanceHistoryDto {
			return BalanceHistoryDto(
				0,
				balanceId,
				userId,
				updateAmount,
				LocalDateTime.now()
			)
		}
	}
}