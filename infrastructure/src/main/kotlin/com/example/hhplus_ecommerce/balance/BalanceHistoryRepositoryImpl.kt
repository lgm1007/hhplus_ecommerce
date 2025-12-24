package com.example.hhplus_ecommerce.balance

import com.example.hhplus_ecommerce.balance.entity.BalanceHistoryEntity
import org.springframework.stereotype.Repository

@Repository
class BalanceHistoryRepositoryImpl(private val balanceHistoryJpaRepository: BalanceHistoryJpaRepository) : BalanceHistoryRepository {
	override fun insert(balanceHistory: BalanceHistory): BalanceHistory {
		return balanceHistoryJpaRepository.save(BalanceHistoryEntity.from(balanceHistory))
			.toDomain()
	}
}