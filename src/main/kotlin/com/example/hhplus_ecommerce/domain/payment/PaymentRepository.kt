package com.example.hhplus_ecommerce.domain.payment

import com.example.hhplus_ecommerce.domain.payment.dto.PaymentDto
import com.example.hhplus_ecommerce.infrastructure.payment.entity.PaymentEntity

interface PaymentRepository {
	fun insert(paymentDto: PaymentDto): PaymentEntity
}