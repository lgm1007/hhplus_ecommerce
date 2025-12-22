package com.example.hhplus_ecommerce.balance

import com.example.hhplus_ecommerce.domain.balance.dto.BalanceDto
import com.example.hhplus_ecommerce.infrastructure.balance.entity.BalanceEntity

interface BalanceRepository {
	fun getByUserId(userId: Long): BalanceEntity

	fun insert(balanceDto: BalanceDto): BalanceEntity

	fun updateDecreaseAmount(userId: Long, amount: Int): BalanceEntity

	fun updateChargeAmount(userId: Long, amount: Int): BalanceEntity

	fun deleteAll()
}