package com.example.hhplus_ecommerce.domain.balance.dto

import com.example.hhplus_ecommerce.infrastructure.balance.entity.BalanceEntity
import java.time.LocalDateTime

class BalanceDto(
	val balanceId: Long,
	val userId: Long,
	val amount: Int,
	val createdDate: LocalDateTime,
	val lastModifiedDate: LocalDateTime
) {

	companion object {
		fun from(balanceEntity: BalanceEntity): BalanceDto {
			return BalanceDto(
				balanceEntity.id,
				balanceEntity.userId,
				balanceEntity.amount,
				balanceEntity.createdDate,
				balanceEntity.lastModifiedDate
			)
		}
	}
}