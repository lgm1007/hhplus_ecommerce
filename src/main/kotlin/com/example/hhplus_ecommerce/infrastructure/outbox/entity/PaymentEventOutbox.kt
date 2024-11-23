package com.example.hhplus_ecommerce.infrastructure.outbox.entity

import com.example.hhplus_ecommerce.domain.outbox.OutboxEventStatus
import com.example.hhplus_ecommerce.domain.outbox.dto.PaymentEventOutboxDto
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
class PaymentEventOutbox(
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

	fun modifyEventStatus(eventStatus: OutboxEventStatus) {
		this.eventStatus = eventStatus
	}

	companion object {
		fun from(paymentEventOutboxDto: PaymentEventOutboxDto): PaymentEventOutbox {
			return PaymentEventOutbox(
				userId = paymentEventOutboxDto.userId,
				orderId = paymentEventOutboxDto.orderId,
				eventStatus = paymentEventOutboxDto.eventStatus
			)
		}
	}
}