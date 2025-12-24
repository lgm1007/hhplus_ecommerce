package com.example.hhplus_ecommerce.outbox

data class ProductOrderEventOutboxPatch(
	val userId: Long,
	val productDetailId: Long,
	val orderQuantity: Int,
	val eventStatus: OutboxEventStatus
) {
}