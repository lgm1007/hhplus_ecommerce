package com.example.hhplus_ecommerce.outbox

data class PaymentEventOutboxPatch(
	val userId: Long,
	val orderId: Long,
	val eventStatus: OutboxEventStatus
) {
}