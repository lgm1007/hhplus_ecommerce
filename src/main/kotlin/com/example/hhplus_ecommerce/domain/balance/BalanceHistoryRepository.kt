package com.example.hhplus_ecommerce.domain.balance

import com.example.hhplus_ecommerce.domain.balance.dto.BalanceHistoryDto

interface BalanceHistoryRepository {
	fun insert(balanceHistoryDto: BalanceHistoryDto): BalanceHistoryDto
}