package com.example.hhplus_ecommerce.outbox

import com.example.hhplus_ecommerce.outbox.entity.ProductOrderEventOutboxEntity
import com.example.hhplus_ecommerce.share.exception.ErrorStatus
import com.example.hhplus_ecommerce.share.exception.NotFoundException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class ProductOrderEventOutboxRepositoryImpl(
	private val jpaRepository: ProductOrderEventOutboxJpaRepository
) : ProductOrderEventOutboxRepository {
	override fun getAllByEventStatus(
		eventStatus: OutboxEventStatus
	): List<ProductOrderEventOutbox> {
		val findEntities = jpaRepository.findAllByEventStatus(eventStatus)
		return findEntities.map { it.toDomain() }
	}

	override fun getAllByUserIdAndProductDetailId(
		userId: Long,
		productDetailId: Long
	): List<ProductOrderEventOutbox> {
		val findEntities = jpaRepository.findAllByUserIdAndProductDetailId(userId, productDetailId)
		return findEntities.map { it.toDomain() }
	}

	override fun insert(productOrderEventOutbox: ProductOrderEventOutbox) {
		jpaRepository.save(ProductOrderEventOutboxEntity.from(productOrderEventOutbox))
	}

	override fun insertAll(productOrderEventOutboxes: List<ProductOrderEventOutbox>) {
		jpaRepository.saveAll(ProductOrderEventOutboxEntity.fromList(productOrderEventOutboxes))
	}

	@Transactional
	override fun updateStatus(
		outboxPatch: ProductOrderEventOutboxPatch
	): ProductOrderEventOutbox {
		val findEntity = jpaRepository.findAllByUserIdAndProductDetailId(
			outboxPatch.userId, outboxPatch.productDetailId
		)
		.filter { it.orderQuantity == outboxPatch.orderQuantity && it.eventStatus != outboxPatch.eventStatus }
		.sortedByDescending { it.createdDate }
		.firstOrNull()
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_PRODUCT_ORDER_OUTBOX)
		val productOrderEventOutbox = findEntity.toDomain()

		productOrderEventOutbox.modifyEventStatus(outboxPatch.eventStatus)
		findEntity.eventStatus = productOrderEventOutbox.eventStatus

		return productOrderEventOutbox
	}

	override fun deleteById(outboxId: Long) {
		jpaRepository.deleteById(outboxId)
	}

	override fun deleteAll() {
		jpaRepository.deleteAll()
	}
}