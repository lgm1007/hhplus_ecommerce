package com.example.hhplus_ecommerce.balance.request

data class BalanceChargeRequest(
	val userId: Long,
	val amount: Int
)