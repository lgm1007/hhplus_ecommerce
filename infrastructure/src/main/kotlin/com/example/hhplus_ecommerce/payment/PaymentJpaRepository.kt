package com.example.hhplus_ecommerce.payment

import com.example.hhplus_ecommerce.payment.entity.PaymentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentJpaRepository : JpaRepository<PaymentEntity, Long> {
}