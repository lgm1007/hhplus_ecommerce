package com.example.hhplus_ecommerce.domain.outbox

import com.example.hhplus_ecommerce.domain.outbox.dto.PaymentEventOutboxDto

interface PaymentEventOutboxRepository {
	fun getAllByUserIdAndEventStatus(userId: Long, eventStatus: OutboxEventStatus): List<PaymentEventOutboxDto>

	fun insert(paymentEventOutboxDto: PaymentEventOutboxDto)
}