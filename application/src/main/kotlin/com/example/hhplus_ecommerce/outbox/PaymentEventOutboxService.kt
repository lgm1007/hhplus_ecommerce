package com.example.hhplus_ecommerce.outbox

import com.example.hhplus_ecommerce.outbox.dto.PaymentEventOutboxDto
import com.example.hhplus_ecommerce.outbox.dto.PaymentEventOutboxRequestDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PaymentEventOutboxService(
	private val paymentEventOutboxRepository: PaymentEventOutboxRepository
) {
	@Transactional(readOnly = true)
	fun getAllByEventStatus(eventStatus: OutboxEventStatus): List<PaymentEventOutboxDto> {
		return paymentEventOutboxRepository.getAllByEventStatus(eventStatus).map { PaymentEventOutboxDto.from(it) }
	}

	@Transactional
	fun save(outbox: PaymentEventOutboxDto) {
		paymentEventOutboxRepository.insert(
			PaymentEventOutbox(
				userId = outbox.userId,
				orderId = outbox.orderId,
				eventStatus = outbox.eventStatus
			)
		)
	}

	@Transactional
	fun updateEventStatus(requestDto: PaymentEventOutboxRequestDto): PaymentEventOutboxDto {
		return paymentEventOutboxRepository.updateStatus(
			PaymentEventOutboxPatch(
				userId = requestDto.userId,
				orderId = requestDto.orderId,
				eventStatus = requestDto.eventStatus
			)
		).let { PaymentEventOutboxDto.from(it) }
	}

	@Transactional
	fun deleteById(outboxId: Long) {
		return paymentEventOutboxRepository.deleteById(outboxId)
	}
}