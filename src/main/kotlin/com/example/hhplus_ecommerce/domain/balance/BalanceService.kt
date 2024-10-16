package com.example.hhplus_ecommerce.domain.balance

import com.example.hhplus_ecommerce.domain.balance.dto.BalanceDto
import com.example.hhplus_ecommerce.domain.balance.dto.BalanceHistoryDto
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
		val balanceDto = BalanceDto.from(balanceRepository.updateDecreaseAmount(userId, amount))
		balanceHistoryRepository.insert(BalanceHistoryDto.of(balanceDto.balanceId, userId, (amount * -1)))
		return balanceDto
	}

	@Transactional
	fun updateAmountCharge(userId: Long, amount: Int): BalanceDto {
		val balanceDto = BalanceDto.from(balanceRepository.updateChargeAmount(userId, amount))
		balanceHistoryRepository.insert(BalanceHistoryDto.of(balanceDto.balanceId, userId, amount))
		return balanceDto
	}
}