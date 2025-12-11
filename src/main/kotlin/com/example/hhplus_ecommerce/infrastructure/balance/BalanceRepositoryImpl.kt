package com.example.hhplus_ecommerce.infrastructure.balance

import com.example.hhplus_ecommerce.domain.share.exception.ErrorStatus
import com.example.hhplus_ecommerce.domain.balance.BalanceRepository
import com.example.hhplus_ecommerce.domain.balance.dto.BalanceDto
import com.example.hhplus_ecommerce.domain.share.exception.NotFoundException
import com.example.hhplus_ecommerce.infrastructure.balance.entity.BalanceEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class BalanceRepositoryImpl(private val balanceJpaRepository: BalanceJpaRepository) : BalanceRepository {
	@Transactional
	override fun getByUserId(userId: Long): BalanceEntity {
		return balanceJpaRepository.findByUserId(userId)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_USER_BALANCE)
	}

	override fun insert(balanceDto: BalanceDto): BalanceEntity {
		return balanceJpaRepository.save(BalanceEntity.from(balanceDto))
	}

	@Transactional
	override fun updateDecreaseAmount(userId: Long, amount: Int): BalanceEntity {
		val balance = (balanceJpaRepository.findByUserId(userId)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_USER_BALANCE))

		balance.decreaseAmount(amount)

		return balanceJpaRepository.save(balance)
	}

	@Transactional
	override fun updateChargeAmount(userId: Long, amount: Int): BalanceEntity {
		val balance = balanceJpaRepository.findByUserId(userId)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_USER_BALANCE)

		balance.chargeAmount(amount)

		return balanceJpaRepository.save(balance)
	}

	@Transactional
	override fun deleteAll() {
		balanceJpaRepository.deleteAll()
	}
}