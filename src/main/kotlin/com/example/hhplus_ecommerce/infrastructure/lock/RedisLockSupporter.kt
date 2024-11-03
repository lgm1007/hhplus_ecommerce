package com.example.hhplus_ecommerce.infrastructure.lock

import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class RedisLockSupporter(
	private val redisTemplate: RedisTemplate<String, String>,
	private val redissonClient: RedissonClient
) {
	/**
	 * Lettuce 사용하여 락 습득, 해제 구현
	 */
	fun lock(key: Any, timeout: Long = 3000): Boolean {
		// key: key.toString(), value: lock, ttl: 3000 millis
		return redisTemplate
			.opsForValue()
			.setIfAbsent(key.toString(), "lock", Duration.ofMillis(timeout))
			?: false
	}

	fun unlock(key: Any): Boolean {
		return redisTemplate.delete(key.toString())
	}

	/**
	 * Redisson 으로 Pub&Sub RLock 제공
	 */
	fun getRLock(key: String): RLock {
		return redissonClient.getLock(key)
	}
}