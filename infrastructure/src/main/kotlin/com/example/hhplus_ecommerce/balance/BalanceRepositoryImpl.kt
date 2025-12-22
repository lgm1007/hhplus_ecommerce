package com.example.hhplus_ecommerce.balance

import com.example.hhplus_ecommerce.balance.entity.BalanceEntity
import com.example.hhplus_ecommerce.share.exception.ErrorStatus
import com.example.hhplus_ecommerce.share.exception.NotFoundException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class BalanceRepositoryImpl(private val balanceJpaRepository: BalanceJpaRepository) : BalanceRepository {
	@Transactional
	override fun getByUserId(userId: Long): Balance {
		val findEntity = balanceJpaRepository.findByUserId(userId)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_USER_BALANCE)

		return findEntity.toDomain()
	}

	override fun insert(balance: Balance): Balance {
		return balanceJpaRepository.save(BalanceEntity.from(balance))
			.toDomain()
	}

	@Transactional
	override fun updateDecreaseAmount(userId: Long, amount: Int): Balance {
		val findEntity = balanceJpaRepository.findByUserId(userId)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_USER_BALANCE)
		val balance = findEntity.toDomain()

		balance.decreaseAmount(amount)
		findEntity.amount = balance.amount

		return balance
	}

	@Transactional
	override fun updateChargeAmount(userId: Long, amount: Int): Balance {
		val findEntity = balanceJpaRepository.findByUserId(userId)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_USER_BALANCE)
		val balance = this.getByUserId(userId)

		balance.chargeAmount(amount)
		findEntity.amount = balance.amount

		return balance
	}

	@Transactional
	override fun deleteAll() {
		balanceJpaRepository.deleteAll()
	}
}