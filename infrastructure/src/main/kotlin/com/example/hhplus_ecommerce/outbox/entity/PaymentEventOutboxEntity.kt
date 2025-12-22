package com.example.hhplus_ecommerce.outbox.entity

import com.example.hhplus_ecommerce.outbox.OutboxEventStatus
import com.example.hhplus_ecommerce.outbox.PaymentEventOutbox
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "PAYMENTEVENTOUTBOX")
class PaymentEventOutboxEntity(
	val userId: Long,
	val orderId: Long,
	@Enumerated(EnumType.STRING)
	var eventStatus: OutboxEventStatus
) {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0

	@CreatedDate
	var createdDate: LocalDateTime = LocalDateTime.now()
		private set

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