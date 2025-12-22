package com.example.hhplus_ecommerce.outbox

interface ProductOrderEventOutboxRepository {
	fun getAllByEventStatus(eventStatus: OutboxEventStatus): List<ProductOrderEventOutbox>

	fun getAllByUserIdAndProductDetailId(userId: Long, productDetailId: Long): List<ProductOrderEventOutbox>

	fun insert(productOrderEventOutbox: ProductOrderEventOutbox)

	fun insertAll(productOrderEventOutboxes: List<ProductOrderEventOutbox>)

	fun updateStatus(productOrderEventOutboxPatch: ProductOrderEventOutboxPatch): ProductOrderEventOutbox

	fun deleteById(outboxId: Long)

	fun deleteAll()
}