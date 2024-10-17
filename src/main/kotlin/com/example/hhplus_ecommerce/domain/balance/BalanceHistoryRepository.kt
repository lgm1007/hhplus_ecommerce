package com.example.hhplus_ecommerce.domain.balance

import com.example.hhplus_ecommerce.domain.balance.dto.BalanceHistoryDto
import com.example.hhplus_ecommerce.infrastructure.balance.entity.BalanceHistory

interface BalanceHistoryRepository {
	fun insert(balanceHistoryDto: BalanceHistoryDto): BalanceHistory
}