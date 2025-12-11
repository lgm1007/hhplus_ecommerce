package com.example.hhplus_ecommerce.infrastructure.balance

import com.example.hhplus_ecommerce.infrastructure.balance.entity.BalanceEntity
import org.springframework.data.jpa.repository.JpaRepository

interface BalanceJpaRepository : JpaRepository<BalanceEntity, Long> {
	fun findByUserId(userId: Long): BalanceEntity?
}