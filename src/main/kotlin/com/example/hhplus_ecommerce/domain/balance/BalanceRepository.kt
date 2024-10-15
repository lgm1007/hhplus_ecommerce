package com.example.hhplus_ecommerce.domain.balance

import com.example.hhplus_ecommerce.domain.balance.dto.BalanceDto
import com.example.hhplus_ecommerce.infrastructure.balance.entity.Balance

interface BalanceRepository {
	fun getByUserIdWithLock(userId: Long): Balance

	fun insert(balanceDto: BalanceDto): Balance

	fun updateAmountDecrease(userId: Long, amount: Int): Balance
}