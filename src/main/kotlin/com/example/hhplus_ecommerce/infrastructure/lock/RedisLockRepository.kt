package com.example.hhplus_ecommerce.infrastructure.lock

import com.example.hhplus_ecommerce.domain.lock.LockRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class RedisLockRepository(private val redisTemplate: RedisTemplate<String, String>) : LockRepository {
	/**
	 * Lettuce 사용하여 락 습득, 해제 구현
	 */
	override fun lock(key: Any, timeout: Long): Boolean {
		// key: key.toString(), value: lock, ttl: 3000 millis
		return redisTemplate
			.opsForValue()
			.setIfAbsent(key.toString(), "lock", Duration.ofMillis(timeout))
			?: false
	}

	override fun unlock(key: Any): Boolean {
		return redisTemplate.delete(key.toString())
	}
}