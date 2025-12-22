package com.example.hhplus_ecommerce.user

import java.time.LocalDateTime

data class User(
	val userId: Long? = null,
	val name: String,
	val email: String,
	var createdDate: LocalDateTime? = null
)
