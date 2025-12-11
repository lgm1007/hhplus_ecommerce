package com.example.hhplus_ecommerce.domain.outbox.dto

import com.example.hhplus_ecommerce.domain.outbox.OutboxEventStatus
import com.example.hhplus_ecommerce.infrastructure.outbox.entity.PaymentEventOutboxEntity
import java.time.LocalDateTime

data class PaymentEventOutboxDto(
	val id: Long,
	val userId: Long,
	val orderId: Long,
	val eventStatus: OutboxEventStatus,
	val createdDate: LocalDateTime
) {
	companion object {
		fun from(paymentEventOutboxEntity: PaymentEventOutboxEntity): PaymentEventOutboxDto {
			return PaymentEventOutboxDto(
				paymentEventOutboxEntity.id,
				paymentEventOutboxEntity.userId,
				paymentEventOutboxEntity.orderId,
				paymentEventOutboxEntity.eventStatus,
				paymentEventOutboxEntity.createdDate
			)
		}

		fun fromList(paymentEventOutboxEntities: List<PaymentEventOutboxEntity>): List<PaymentEventOutboxDto> {
			return paymentEventOutboxEntities.map(::from)
		}
	}
}