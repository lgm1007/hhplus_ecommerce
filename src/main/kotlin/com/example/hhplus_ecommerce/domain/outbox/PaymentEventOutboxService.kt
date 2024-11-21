package com.example.hhplus_ecommerce.domain.outbox

import com.example.hhplus_ecommerce.domain.outbox.dto.PaymentEventOutboxDto
import com.example.hhplus_ecommerce.domain.outbox.dto.PaymentEventOutboxRequestDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PaymentEventOutboxService(
	private val paymentEventOutboxRepository: PaymentEventOutboxRepository
) {
	fun getAllByEventStatus(eventStatus: OutboxEventStatus): List<PaymentEventOutboxDto> {
		return paymentEventOutboxRepository.getAllByEventStatus(eventStatus)
	}

	fun save(outbox: PaymentEventOutboxDto) {
		paymentEventOutboxRepository.insert(outbox)
	}

	@Transactional
	fun updateEventStatusPublish(paymentEventOutboxRequest: PaymentEventOutboxRequestDto): PaymentEventOutboxDto {
		return paymentEventOutboxRepository.updateStatus(paymentEventOutboxRequest, OutboxEventStatus.PUBLISH)
	}

	@Transactional
	fun updateEventStatusComplete(paymentEventOutboxRequest: PaymentEventOutboxRequestDto): PaymentEventOutboxDto {
		return paymentEventOutboxRepository.updateStatus(paymentEventOutboxRequest, OutboxEventStatus.COMPLETE)
	}

	@Transactional
	fun deleteById(outboxId: Long) {
		return paymentEventOutboxRepository.deleteById(outboxId)
	}
}