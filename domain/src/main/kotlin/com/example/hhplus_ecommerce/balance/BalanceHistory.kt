package com.example.hhplus_ecommerce.balance

import java.time.LocalDateTime

data class BalanceHistory(
	val balanceHistoryId: Long? = null,
	val balanceId: Long,
	val userId: Long,
	val updateAmount: Int,
	var createdDate: LocalDateTime? = null
)