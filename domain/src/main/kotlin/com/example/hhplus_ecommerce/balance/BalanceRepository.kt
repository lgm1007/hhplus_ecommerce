package com.example.hhplus_ecommerce.balance

interface BalanceRepository {
	fun getByUserId(userId: Long): Balance

	fun insert(balance: Balance): Balance

	fun updateDecreaseAmount(userId: Long, amount: Int): Balance

	fun updateChargeAmount(userId: Long, amount: Int): Balance

	fun deleteAll()
}