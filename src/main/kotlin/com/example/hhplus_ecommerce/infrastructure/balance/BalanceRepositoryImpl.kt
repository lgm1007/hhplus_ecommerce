package com.example.hhplus_ecommerce.infrastructure.balance

import com.example.hhplus_ecommerce.api.error.ErrorStatus
import com.example.hhplus_ecommerce.domain.balance.BalanceRepository
import com.example.hhplus_ecommerce.domain.balance.dto.BalanceDto
import com.example.hhplus_ecommerce.exception.NotFoundException
import com.example.hhplus_ecommerce.infrastructure.balance.entity.Balance
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class BalanceRepositoryImpl(private val balanceJpaRepository: BalanceJpaRepository) : BalanceRepository {
	override fun getByUserIdWithLock(userId: Long): Balance {
		return balanceJpaRepository.findByUserIdWithLock(userId)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_USER_BALANCE)
	}

	override fun insert(balanceDto: BalanceDto): Balance {
		return balanceJpaRepository.save(Balance.from(balanceDto))
	}

	@Transactional
	override fun updateDecreaseAmount(userId: Long, amount: Int): Balance {
		val balance = (balanceJpaRepository.findByUserIdWithLock(userId)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_USER_BALANCE))

		balance.decreaseAmount(amount)

		return balanceJpaRepository.save(balance)
	}

	@Transactional
	override fun updateChargeAmount(userId: Long, amount: Int): Balance {
		val balance = balanceJpaRepository.findByUserIdWithLock(userId)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_USER_BALANCE)

		balance.chargeAmount(amount)

		return balanceJpaRepository.save(balance)
	}
}