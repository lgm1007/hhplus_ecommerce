package com.example.hhplus_ecommerce.balance.dto

import com.example.hhplus_ecommerce.balance.BalanceHistory
import java.time.LocalDateTime

class BalanceHistoryDto(
	val balanceHistoryId: Long? = null,
	val balanceId: Long,
	val userId: Long,
	val updateAmount: Int,
	val createdDate: LocalDateTime? = null
) {
	companion object {
		fun from(balanceHistory: BalanceHistory): BalanceHistoryDto {
			return BalanceHistoryDto(
				balanceHistoryId = balanceHistory.balanceHistoryId,
				balanceId = balanceHistory.balanceId,
				userId = balanceHistory.userId,
				updateAmount = balanceHistory.updateAmount,
				createdDate = balanceHistory.createdDate
			)
		}

		fun of(balanceId: Long, userId: Long, updateAmount: Int): BalanceHistoryDto {
			return BalanceHistoryDto(
				balanceHistoryId = 0,
				balanceId = balanceId,
				userId = userId,
				updateAmount = updateAmount,
				createdDate = LocalDateTime.now()
			)
		}
	}
}