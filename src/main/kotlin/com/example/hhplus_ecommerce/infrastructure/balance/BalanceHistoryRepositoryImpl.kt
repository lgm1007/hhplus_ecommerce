package com.example.hhplus_ecommerce.infrastructure.balance

import com.example.hhplus_ecommerce.domain.balance.BalanceHistoryRepository
import com.example.hhplus_ecommerce.domain.balance.dto.BalanceHistoryDto
import com.example.hhplus_ecommerce.infrastructure.balance.entity.BalanceHistory

class BalanceHistoryRepositoryImpl(private val balanceHistoryJpaRepository: BalanceHistoryJpaRepository) : BalanceHistoryRepository {
	override fun insert(balanceHistoryDto: BalanceHistoryDto): BalanceHistoryDto {
		return BalanceHistoryDto.from(balanceHistoryJpaRepository.save(BalanceHistory.from(balanceHistoryDto)))
	}
}