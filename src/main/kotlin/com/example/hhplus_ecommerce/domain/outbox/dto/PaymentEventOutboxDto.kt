package com.example.hhplus_ecommerce.domain.outbox.dto

import com.example.hhplus_ecommerce.domain.outbox.OutboxEventStatus
import com.example.hhplus_ecommerce.infrastructure.outbox.entity.PaymentEventOutbox
import java.time.LocalDateTime

data class PaymentEventOutboxDto(
	val id: Long,
	val userId: Long,
	val orderId: Long,
	val eventStatus: OutboxEventStatus,
	val createdDate: LocalDateTime
) {
	companion object {
		fun from(paymentEventOutbox: PaymentEventOutbox): PaymentEventOutboxDto {
			return PaymentEventOutboxDto(
				paymentEventOutbox.id,
				paymentEventOutbox.userId,
				paymentEventOutbox.orderId,
				paymentEventOutbox.eventStatus,
				paymentEventOutbox.createdDate
			)
		}

		fun fromList(paymentEventOutboxs: List<PaymentEventOutbox>): List<PaymentEventOutboxDto> {
			return paymentEventOutboxs.map(::from)
		}
	}
}