package com.example.hhplus_ecommerce.application.balance

import com.example.hhplus_ecommerce.domain.balance.BalanceHistoryRepository
import com.example.hhplus_ecommerce.domain.balance.BalanceRepository
import com.example.hhplus_ecommerce.domain.balance.dto.BalanceDto
import com.example.hhplus_ecommerce.domain.balance.dto.BalanceHistoryDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BalanceService(
	private val balanceRepository: BalanceRepository,
	private val balanceHistoryRepository: BalanceHistoryRepository
) {
	fun getByUserId(userId: Long): BalanceDto {
		return BalanceDto.Companion.from(balanceRepository.getByUserId(userId))
	}

	@Transactional
	fun updateAmountDecrease(userId: Long, amount: Int): BalanceDto {
		val balanceDto = BalanceDto.Companion.from(balanceRepository.updateDecreaseAmount(userId, amount))
		balanceHistoryRepository.insert(BalanceHistoryDto.Companion.of(balanceDto.balanceId, userId, (amount * -1)))
		return balanceDto
	}

	@Transactional
	fun updateAmountCharge(userId: Long, amount: Int): BalanceDto {
		val balanceDto = BalanceDto.Companion.from(balanceRepository.updateChargeAmount(userId, amount))
		balanceHistoryRepository.insert(BalanceHistoryDto.Companion.of(balanceDto.balanceId, userId, amount))
		return balanceDto
	}
}