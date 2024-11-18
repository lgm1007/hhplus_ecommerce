package com.example.hhplus_ecommerce.infrastructure.outbox

import com.example.hhplus_ecommerce.domain.outbox.OutboxEventStatus
import com.example.hhplus_ecommerce.domain.outbox.PaymentEventOutboxRepository
import com.example.hhplus_ecommerce.domain.outbox.dto.PaymentEventOutboxDto
import org.springframework.stereotype.Repository

@Repository
class PaymentEventOutboxRepositoryImpl(
	private val jpaRepository: PaymentEventOutboxJpaRepository
) : PaymentEventOutboxRepository {
	override fun getAllByUserIdAndEventStatus(
		userId: Long,
		eventStatus: OutboxEventStatus
	): List<PaymentEventOutboxDto> {
		return PaymentEventOutboxDto.fromList(jpaRepository.findAllByUserIdAndEventStatus(userId, eventStatus))
	}

	override fun insert(paymentEventOutboxDto: PaymentEventOutboxDto) {
		TODO("Not yet implemented")
	}
}