package com.example.hhplus_ecommerce.infrastructure.balance

import com.example.hhplus_ecommerce.infrastructure.balance.entity.Balance
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import javax.persistence.LockModeType

interface BalanceJpaRepository : JpaRepository<Balance, Long> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT b FROM Balance b WHERE b.userId = :userId")
	fun findByUserIdWithLock(@Param("userId") userId: Long): Balance?
}