package com.example.hhplus_ecommerce.outbox

import java.time.LocalDateTime

data class ProductOrderEventOutbox(
	val productOrderEventOutboxId: Long? = null,
	val userId: Long,
	val productDetailId: Long,
	val orderQuantity: Int,
	var eventStatus: OutboxEventStatus,
	var createdDate: LocalDateTime? = null
) {
	fun modifyEventStatus(eventStatus: OutboxEventStatus) {
		this.eventStatus = eventStatus
	}
}
