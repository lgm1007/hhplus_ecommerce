package com.example.hhplus_ecommerce.balance

import com.example.hhplus_ecommerce.balance.entity.BalanceHistoryEntity
import org.springframework.data.jpa.repository.JpaRepository

interface BalanceHistoryJpaRepository : JpaRepository<BalanceHistoryEntity, Long> {
}