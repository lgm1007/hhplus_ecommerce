package com.example.hhplus_ecommerce.infrastructure.payment

import com.example.hhplus_ecommerce.domain.payment.PaymentRepository
import com.example.hhplus_ecommerce.domain.payment.dto.PaymentDto
import com.example.hhplus_ecommerce.infrastructure.payment.entity.Payment
import org.springframework.stereotype.Repository

@Repository
class PaymentRepositoryImpl(private val paymentJpaRepository: PaymentJpaRepository) : PaymentRepository {
	override fun insert(paymentDto: PaymentDto): Payment {
		return paymentJpaRepository.save(Payment.from(paymentDto))
	}
}