package com.example.hhplus_ecommerce.outbox

import com.example.hhplus_ecommerce.outbox.entity.PaymentEventOutboxEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentEventOutboxJpaRepository : JpaRepository<PaymentEventOutboxEntity, Long> {
	fun findAllByEventStatus(eventStatus: OutboxEventStatus): List<PaymentEventOutboxEntity>

	fun findByUserIdAndOrderId(userId: Long, orderId: Long): PaymentEventOutboxEntity?
}