package com.example.hhplus_ecommerce.domain.user.dto

import java.time.LocalDateTime

class UserDto(
	val id: Long,
	val name: String,
	val email: String,
	val createdDate: LocalDateTime
) {
}