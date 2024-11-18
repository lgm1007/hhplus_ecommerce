package com.example.hhplus_ecommerce.domain.outbox

import com.example.hhplus_ecommerce.domain.outbox.dto.ProductOrderEventOutboxDto

interface ProductOrderEventOutboxRepository {
	fun getAllByUserIdAndEventStatus(userId: Long, eventStatus: OutboxEventStatus): List<ProductOrderEventOutboxDto>

	fun insert(productOrderEventOutboxDto: ProductOrderEventOutboxDto)
}