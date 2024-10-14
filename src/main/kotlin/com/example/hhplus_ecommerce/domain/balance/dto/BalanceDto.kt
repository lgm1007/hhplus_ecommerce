package com.example.hhplus_ecommerce.domain.balance.dto

import com.example.hhplus_ecommerce.api.error.ErrorStatus
import com.example.hhplus_ecommerce.exception.BadRequestException
import com.example.hhplus_ecommerce.infrastructure.balance.entity.Balance
import java.time.LocalDateTime

class BalanceDto(
	val balanceId: Long,
	val userId: Long,
	var amount: Int,
	val createdDate: LocalDateTime,
	val lastModifiedDate: LocalDateTime
) {
	fun decreaseAmount(amount: Int) {
		// 현재 있는 잔액보다 더 큰 값을 차감하려 하면 잔액 부족 예외!
		if (this.amount - amount < 0) {
			throw BadRequestException(ErrorStatus.NOT_ENOUGH_BALANCE)
		}

		this.amount -= amount
	}

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