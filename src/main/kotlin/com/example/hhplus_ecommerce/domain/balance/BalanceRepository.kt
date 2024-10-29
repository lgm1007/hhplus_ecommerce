package com.example.hhplus_ecommerce.domain.balance

import com.example.hhplus_ecommerce.domain.balance.dto.BalanceDto
import com.example.hhplus_ecommerce.infrastructure.balance.entity.Balance

interface BalanceRepository {
	fun getByUserId(userId: Long): Balance

	fun insert(balanceDto: BalanceDto): Balance

	fun updateDecreaseAmount(userId: Long, amount: Int): Balance

	fun updateChargeAmount(userId: Long, amount: Int): Balance
}