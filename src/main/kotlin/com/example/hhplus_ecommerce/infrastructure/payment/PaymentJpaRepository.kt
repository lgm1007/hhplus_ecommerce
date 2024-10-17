package com.example.hhplus_ecommerce.infrastructure.payment

import com.example.hhplus_ecommerce.infrastructure.payment.entity.Payment
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentJpaRepository : JpaRepository<Payment, Long> {
}