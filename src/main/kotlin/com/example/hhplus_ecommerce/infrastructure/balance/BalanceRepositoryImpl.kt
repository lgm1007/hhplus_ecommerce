package com.example.hhplus_ecommerce.infrastructure.balance

import com.example.hhplus_ecommerce.api.error.ErrorStatus
import com.example.hhplus_ecommerce.domain.balance.BalanceRepository
import com.example.hhplus_ecommerce.domain.balance.dto.BalanceDto
import com.example.hhplus_ecommerce.exception.NotFoundException
import com.example.hhplus_ecommerce.infrastructure.balance.entity.Balance
import org.springframework.stereotype.Repository

@Repository
class BalanceRepositoryImpl(private val balanceJpaRepository: BalanceJpaRepository) : BalanceRepository {
	override fun getByUserId(userId: Long): Balance {
		return balanceJpaRepository.findByUserId(userId)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_USER_BALANCE)
	}

	override fun insert(balanceDto: BalanceDto): Balance {
		return balanceJpaRepository.save(Balance.from(balanceDto))
	}
}