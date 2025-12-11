package com.example.hhplus_ecommerce.application.payment

import com.example.hhplus_ecommerce.domain.payment.PaymentRepository
import com.example.hhplus_ecommerce.domain.payment.dto.PaymentDto
import org.springframework.stereotype.Service

@Service
class PaymentService(private val paymentRepository: PaymentRepository) {
	fun registerPayment(paymentDto: PaymentDto): PaymentDto {
		return PaymentDto.Companion.from(paymentRepository.insert(paymentDto))
	}
}