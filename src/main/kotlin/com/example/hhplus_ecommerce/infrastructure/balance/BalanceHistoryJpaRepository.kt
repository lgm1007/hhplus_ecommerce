package com.example.hhplus_ecommerce.infrastructure.balance

import com.example.hhplus_ecommerce.infrastructure.balance.entity.BalanceHistory
import org.springframework.data.jpa.repository.JpaRepository

interface BalanceHistoryJpaRepository : JpaRepository<BalanceHistory, Long> {
}