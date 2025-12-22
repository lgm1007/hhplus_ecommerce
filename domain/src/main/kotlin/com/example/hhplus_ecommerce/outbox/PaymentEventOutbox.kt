package com.example.hhplus_ecommerce.outbox

import java.time.LocalDateTime

data class PaymentEventOutbox(
	val paymentEventOutboxId: Long? = null,
	val userId: Long,
	val orderId: Long,
	var eventStatus: OutboxEventStatus,
	var createdDate: LocalDateTime? = null
)
