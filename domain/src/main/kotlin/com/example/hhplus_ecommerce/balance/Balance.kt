package com.example.hhplus_ecommerce.balance

import com.example.hhplus_ecommerce.share.exception.BadRequestException
import com.example.hhplus_ecommerce.share.exception.ErrorStatus
import java.time.LocalDateTime

data class Balance(
	val balanceId: Long? = null,
	val userId: Long,
	var amount: Int,
	var createdDate: LocalDateTime? = null,
	var lastModifiedDate: LocalDateTime? = null
) {
	/**
	 * 잔액 부족 여부 검사 후 잔액 차감
	 */
	fun decreaseAmount(amount: Int) {
		if (this.amount < amount) {
			throw BadRequestException(ErrorStatus.NOT_ENOUGH_BALANCE)
		}

		this.amount -= amount
	}

	/**
	 * 잔액 충전 금액이 음수인지 검사 후 잔액 충전
	 */
	fun chargeAmount(amount: Int) {
		if (amount < 0) {
			throw BadRequestException(ErrorStatus.CHARGED_AMOUNT_ERROR)
		}

		this.amount += amount
	}
}
