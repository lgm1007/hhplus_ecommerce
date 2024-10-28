package com.example.hhplus_ecommerce.domain.lock

interface LockRepository {
	fun lock(key: Any, timeout: Long = 3000): Boolean

	fun unlock(key: Any): Boolean
}