package com.example.hhplus_ecommerce.balance

interface BalanceHistoryRepository {
	fun insert(balanceHistory: BalanceHistory): BalanceHistory
}