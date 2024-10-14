package com.example.hhplus_ecommerce.domain.balance.dto

import com.example.hhplus_ecommerce.infrastructure.balance.entity.BalanceHistory
import java.time.LocalDateTime

class BalanceHistoryDto(
	val id: Long,
	val balanceId: Long,
	val userId: Long,
	val updateAmount: Int,
	val createdDate: LocalDateTime
) {
	companion object {
		fun from(balanceHistory: BalanceHistory): BalanceHistoryDto {
			return BalanceHistoryDto(
				balanceHistory.id,
				balanceHistory.balanceId,
				balanceHistory.userId,
				balanceHistory.updateAmount,
				balanceHistory.createdDate
			)
		}
	}
}