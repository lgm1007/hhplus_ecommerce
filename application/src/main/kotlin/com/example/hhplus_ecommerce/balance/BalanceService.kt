package com.example.hhplus_ecommerce.balance

import com.example.hhplus_ecommerce.balance.dto.BalanceDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BalanceService(
	private val balanceRepository: BalanceRepository,
	private val balanceHistoryRepository: BalanceHistoryRepository
) {
	fun getByUserId(userId: Long): BalanceDto {
		return BalanceDto.from(balanceRepository.getByUserId(userId))
	}

	@Transactional
	fun updateAmountDecrease(userId: Long, amount: Int): BalanceDto {
		val balanceDto = BalanceDto.from(balanceRepository.updateDecreaseAmount(userId, amount))
		balanceHistoryRepository.insert(
			balanceHistory = BalanceHistory(
				balanceId = balanceDto.balanceId!!,
				userId = userId,
				updateAmount = (amount * -1)
			)
		)
		return balanceDto
	}

	@Transactional
	fun updateAmountCharge(userId: Long, amount: Int): BalanceDto {
		val balanceDto = BalanceDto.from(balanceRepository.updateChargeAmount(userId, amount))
		balanceHistoryRepository.insert(
			balanceHistory = BalanceHistory(
				balanceId = balanceDto.balanceId!!,
				userId = userId,
				updateAmount = amount
			)
		)
		return balanceDto
	}
}