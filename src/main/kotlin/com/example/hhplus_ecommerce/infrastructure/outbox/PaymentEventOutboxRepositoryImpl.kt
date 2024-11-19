package com.example.hhplus_ecommerce.infrastructure.outbox

import com.example.hhplus_ecommerce.domain.outbox.OutboxEventStatus
import com.example.hhplus_ecommerce.domain.outbox.PaymentEventOutboxRepository
import com.example.hhplus_ecommerce.domain.outbox.dto.PaymentEventOutboxDto
import com.example.hhplus_ecommerce.infrastructure.outbox.entity.PaymentEventOutbox
import org.springframework.stereotype.Repository

@Repository
class PaymentEventOutboxRepositoryImpl(
	private val jpaRepository: PaymentEventOutboxJpaRepository
) : PaymentEventOutboxRepository {
	override fun getAllByEventStatus(
		eventStatus: OutboxEventStatus
	): List<PaymentEventOutboxDto> {
		return PaymentEventOutboxDto.fromList(jpaRepository.findAllByEventStatus(eventStatus))
	}

	override fun insert(paymentEventOutboxDto: PaymentEventOutboxDto) {
		jpaRepository.save(PaymentEventOutbox.from(paymentEventOutboxDto))
	}
}