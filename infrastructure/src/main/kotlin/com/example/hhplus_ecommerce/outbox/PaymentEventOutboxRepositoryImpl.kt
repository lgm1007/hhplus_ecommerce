package com.example.hhplus_ecommerce.outbox

import com.example.hhplus_ecommerce.outbox.entity.PaymentEventOutboxEntity
import com.example.hhplus_ecommerce.share.exception.ErrorStatus
import com.example.hhplus_ecommerce.share.exception.NotFoundException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class PaymentEventOutboxRepositoryImpl(
	private val jpaRepository: PaymentEventOutboxJpaRepository
) : PaymentEventOutboxRepository {
	override fun getAllByEventStatus(
		eventStatus: OutboxEventStatus
	): List<PaymentEventOutbox> {
		val findEntities = jpaRepository.findAllByEventStatus(eventStatus)
		return findEntities.map { it.toDomain() }
	}

	override fun getByUserIdAndOrderId(userId: Long, orderId: Long): PaymentEventOutbox {
		val findEntity = jpaRepository.findByUserIdAndOrderId(userId, orderId)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_PAYMENT_OUTBOX)

		return findEntity.toDomain()
	}

	override fun insert(paymentEventOutbox: PaymentEventOutbox) {
		jpaRepository.save(PaymentEventOutboxEntity.from(paymentEventOutbox))
	}

	@Transactional
	override fun updateStatus(
		paymentEventOutboxPatch: PaymentEventOutboxPatch
	): PaymentEventOutbox {
		val findEntity = jpaRepository.findByUserIdAndOrderId(
			paymentEventOutboxPatch.userId,
			paymentEventOutboxPatch.orderId
		) ?: throw NotFoundException(ErrorStatus.NOT_FOUND_PAYMENT_OUTBOX)
		val paymentEventOutbox = findEntity.toDomain()

		paymentEventOutbox.modifyEventStatus(paymentEventOutboxPatch.eventStatus)
		findEntity.eventStatus = paymentEventOutbox.eventStatus

		return paymentEventOutbox
	}

	override fun deleteById(outboxId: Long) {
		jpaRepository.deleteById(outboxId)
	}

	override fun deleteAll() {
		jpaRepository.deleteAll()
	}
}