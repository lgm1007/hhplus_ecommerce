package com.example.hhplus_ecommerce.domain.outbox

import com.example.hhplus_ecommerce.domain.outbox.dto.PaymentEventOutboxDto
import com.example.hhplus_ecommerce.domain.outbox.dto.PaymentEventOutboxRequestDto

interface PaymentEventOutboxRepository {
	fun getAllByEventStatus(eventStatus: OutboxEventStatus): List<PaymentEventOutboxDto>

	fun getByUserIdAndOrderId(userId: Long, orderId: Long): PaymentEventOutboxDto

	fun insert(paymentEventOutboxDto: PaymentEventOutboxDto)

	fun updateStatus(paymentEventOutboxRequest: PaymentEventOutboxRequestDto, eventStatus: OutboxEventStatus): PaymentEventOutboxDto

	fun deleteAll()
}