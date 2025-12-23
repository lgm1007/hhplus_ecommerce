package com.example.hhplus_ecommerce.outbox.dto

import com.example.hhplus_ecommerce.outbox.OutboxEventStatus
import com.example.hhplus_ecommerce.outbox.PaymentEventOutbox
import java.time.LocalDateTime

data class PaymentEventOutboxDto(
	val paymentEventOutboxId: Long? = null,
	val userId: Long,
	val orderId: Long,
	val eventStatus: OutboxEventStatus,
	val createdDate: LocalDateTime? = null
) {
	companion object {
		fun from(paymentEventOutbox: PaymentEventOutbox): PaymentEventOutboxDto {
			return PaymentEventOutboxDto(
				paymentEventOutboxId = paymentEventOutbox.paymentEventOutboxId,
				userId = paymentEventOutbox.userId,
				orderId = paymentEventOutbox.orderId,
				eventStatus = paymentEventOutbox.eventStatus,
				createdDate = paymentEventOutbox.createdDate
			)
		}

		fun fromList(paymentEventOutboxEntities: List<PaymentEventOutbox>): List<PaymentEventOutboxDto> {
			return paymentEventOutboxEntities.map(::from)
		}
	}
}