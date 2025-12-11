package com.example.hhplus_ecommerce.infrastructure.outbox

import com.example.hhplus_ecommerce.domain.share.exception.ErrorStatus
import com.example.hhplus_ecommerce.domain.outbox.OutboxEventStatus
import com.example.hhplus_ecommerce.domain.outbox.PaymentEventOutboxRepository
import com.example.hhplus_ecommerce.domain.outbox.dto.PaymentEventOutboxDto
import com.example.hhplus_ecommerce.domain.outbox.dto.PaymentEventOutboxRequestDto
import com.example.hhplus_ecommerce.domain.share.exception.NotFoundException
import com.example.hhplus_ecommerce.infrastructure.outbox.entity.PaymentEventOutboxEntity
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

	override fun getByUserIdAndOrderId(userId: Long, orderId: Long): PaymentEventOutboxDto {
		return PaymentEventOutboxDto.from(
			jpaRepository.findByUserIdAndOrderId(userId, orderId) ?: throw NotFoundException(ErrorStatus.NOT_FOUND_PAYMENT_OUTBOX)
		)
	}

	override fun insert(paymentEventOutboxDto: PaymentEventOutboxDto) {
		jpaRepository.save(PaymentEventOutboxEntity.from(paymentEventOutboxDto))
	}

	override fun updateStatus(
		paymentEventOutboxRequest: PaymentEventOutboxRequestDto,
		eventStatus: OutboxEventStatus
	): PaymentEventOutboxDto {
		val paymentEventOutbox = jpaRepository.findByUserIdAndOrderId(
			paymentEventOutboxRequest.userId,
			paymentEventOutboxRequest.orderId
		) ?: throw NotFoundException(ErrorStatus.NOT_FOUND_PAYMENT_OUTBOX)
		paymentEventOutbox.modifyEventStatus(eventStatus)

		return PaymentEventOutboxDto.from(paymentEventOutbox)
	}

	override fun deleteById(outboxId: Long) {
		jpaRepository.deleteById(outboxId)
	}

	override fun deleteAll() {
		jpaRepository.deleteAll()
	}
}