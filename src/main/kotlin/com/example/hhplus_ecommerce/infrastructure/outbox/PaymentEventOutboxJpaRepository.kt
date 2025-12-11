package com.example.hhplus_ecommerce.infrastructure.outbox

import com.example.hhplus_ecommerce.domain.outbox.OutboxEventStatus
import com.example.hhplus_ecommerce.infrastructure.outbox.entity.PaymentEventOutboxEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentEventOutboxJpaRepository : JpaRepository<PaymentEventOutboxEntity, Long> {
	fun findAllByEventStatus(eventStatus: OutboxEventStatus): List<PaymentEventOutboxEntity>

	fun findByUserIdAndOrderId(userId: Long, orderId: Long): PaymentEventOutboxEntity?
}