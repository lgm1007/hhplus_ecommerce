package com.example.hhplus_ecommerce.domain.balance

import com.example.hhplus_ecommerce.domain.balance.dto.BalanceDto

interface BalanceRepository {
	fun getByUserId(userId: Long): BalanceDto
}