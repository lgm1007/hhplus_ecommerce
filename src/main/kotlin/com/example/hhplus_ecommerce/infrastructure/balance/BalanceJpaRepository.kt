package com.example.hhplus_ecommerce.infrastructure.balance

import com.example.hhplus_ecommerce.infrastructure.balance.entity.Balance
import org.springframework.data.jpa.repository.JpaRepository

interface BalanceJpaRepository : JpaRepository<Balance, Long> {
	fun findByUserId(userId: Long): Balance?
}