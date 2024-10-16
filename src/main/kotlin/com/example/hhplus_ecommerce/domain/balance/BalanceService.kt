package com.example.hhplus_ecommerce.domain.balance

import com.example.hhplus_ecommerce.domain.balance.dto.BalanceDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BalanceService(
	private val balanceRepository: BalanceRepository,
	private val balanceHistoryRepository: BalanceHistoryRepository
) {
	fun getByUserIdWithLock(userId: Long): BalanceDto {
		return BalanceDto.from(balanceRepository.getByUserIdWithLock(userId))
	}

	fun updateAmountDecrease(userId: Long, amount: Int): BalanceDto {
		return BalanceDto.from(balanceRepository.updateDecreaseAmount(userId, amount))
	}

	@Transactional
	fun updateAmountCharge(userId: Long, amount: Int): BalanceDto {
		return BalanceDto.from(balanceRepository.updateChargeAmount(userId, amount))
	}
}