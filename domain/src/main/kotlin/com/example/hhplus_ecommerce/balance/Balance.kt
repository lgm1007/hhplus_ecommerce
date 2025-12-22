package com.example.hhplus_ecommerce.balance

import com.example.hhplus_ecommerce.share.exception.BadRequestException
import com.example.hhplus_ecommerce.share.exception.ErrorStatus
import java.time.LocalDateTime

data class Balance(
	val balanceId: Long? = null,
	val userId: Long,
	val amount: Int,
	var createdDate: LocalDateTime? = null,
	var lastModifiedDate: LocalDateTime? = null
) {
	/**
	 * 잔액 부족 여부 검사
	 */
	fun validateDecreaseAmount(amount: Int) {
		if (this.amount < amount) {
			throw BadRequestException(ErrorStatus.NOT_ENOUGH_BALANCE)
		}
	}

	/**
	 * 잔액 충전 금액이 음수인지 검사
	 */
	fun validateChargeAmount(amount: Int) {
		if (amount < 0) {
			throw BadRequestException(ErrorStatus.CHARGED_AMOUNT_ERROR)
		}
	}
}
