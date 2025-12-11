package com.example.hhplus_ecommerce.infrastructure.payment

import com.example.hhplus_ecommerce.infrastructure.payment.entity.PaymentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentJpaRepository : JpaRepository<PaymentEntity, Long> {
}