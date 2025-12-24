package com.example.hhplus_ecommerce.outbox.entity

import com.example.hhplus_ecommerce.BaseEntity
import com.example.hhplus_ecommerce.outbox.OutboxEventStatus
import com.example.hhplus_ecommerce.outbox.PaymentEventOutbox
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Table

@Entity
@Table(name = "PAYMENTEVENTOUTBOX")
class PaymentEventOutboxEntity(
	val userId: Long,
	val orderId: Long,
	@Enumerated(EnumType.STRING)
	var eventStatus: OutboxEventStatus
) : BaseEntity() {
	companion object {
		fun from(paymentEventOutbox: PaymentEventOutbox): PaymentEventOutboxEntity {
			return PaymentEventOutboxEntity(
				userId = paymentEventOutbox.userId,
				orderId = paymentEventOutbox.orderId,
				eventStatus = paymentEventOutbox.eventStatus
			)
		}
	}

	fun toDomain(): PaymentEventOutbox {
		return PaymentEventOutbox(
			paymentEventOutboxId = this.id,
			userId = this.userId,
			orderId = this.orderId,
			eventStatus = this.eventStatus,
			createdDate = this.createdDate
		)
	}
}