package com.example.hhplus_ecommerce.outbox

interface PaymentEventOutboxRepository {
	fun getAllByEventStatus(eventStatus: OutboxEventStatus): List<PaymentEventOutbox>

	fun getByUserIdAndOrderId(userId: Long, orderId: Long): PaymentEventOutbox

	fun insert(paymentEventOutbox: PaymentEventOutbox)

	fun updateStatus(paymentEventOutboxPatch: PaymentEventOutboxPatch): PaymentEventOutbox

	fun deleteById(outboxId: Long)

	fun deleteAll()
}