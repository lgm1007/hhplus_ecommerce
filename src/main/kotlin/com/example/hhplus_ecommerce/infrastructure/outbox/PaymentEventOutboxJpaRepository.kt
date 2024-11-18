package com.example.hhplus_ecommerce.infrastructure.outbox

import com.example.hhplus_ecommerce.domain.outbox.OutboxEventStatus
import com.example.hhplus_ecommerce.infrastructure.outbox.entity.PaymentEventOutbox
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentEventOutboxJpaRepository : JpaRepository<PaymentEventOutbox, Long> {
	fun findAllByUserIdAndEventStatus(userId: Long, eventStatus: OutboxEventStatus): List<PaymentEventOutbox>
}