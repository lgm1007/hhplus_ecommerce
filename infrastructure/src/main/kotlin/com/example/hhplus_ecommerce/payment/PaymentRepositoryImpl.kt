package com.example.hhplus_ecommerce.payment

import com.example.hhplus_ecommerce.payment.entity.PaymentEntity
import org.springframework.stereotype.Repository

@Repository
class PaymentRepositoryImpl(private val paymentJpaRepository: PaymentJpaRepository) : PaymentRepository {
	override fun insert(payment: Payment): Payment {
		return paymentJpaRepository.save(PaymentEntity.from(payment))
			.toDomain()
	}
}