package com.example.hhplus_ecommerce.outbox.dto

import com.example.hhplus_ecommerce.outbox.OutboxEventStatus

data class PaymentEventOutboxRequestDto(
	val userId: Long,
	val orderId: Long,
	val eventStatus: OutboxEventStatus
)
