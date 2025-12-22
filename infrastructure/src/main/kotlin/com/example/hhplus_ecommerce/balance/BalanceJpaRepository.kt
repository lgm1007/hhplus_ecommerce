package com.example.hhplus_ecommerce.balance

import com.example.hhplus_ecommerce.balance.entity.BalanceEntity
import org.springframework.data.jpa.repository.JpaRepository

interface BalanceJpaRepository : JpaRepository<BalanceEntity, Long> {
	fun findByUserId(userId: Long): BalanceEntity?
}