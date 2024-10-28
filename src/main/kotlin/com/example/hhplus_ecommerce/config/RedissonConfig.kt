package com.example.hhplus_ecommerce.config

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RedissonConfig {
	@Value("\${spring.redis.host}")
	lateinit var host: String

	@Value("\${spring.redis.port}")
	lateinit var port: String

	@Bean
	fun redissonClient(): RedissonClient {
		val config = Config()
		config.useSingleServer().address = "redis://${host}:${port}"
		return Redisson.create(config)
	}
}