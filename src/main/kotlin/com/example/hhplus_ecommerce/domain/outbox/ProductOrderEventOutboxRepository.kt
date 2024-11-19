package com.example.hhplus_ecommerce.domain.outbox

import com.example.hhplus_ecommerce.domain.outbox.dto.ProductOrderEventOutboxDto

interface ProductOrderEventOutboxRepository {
	fun getAllByEventStatus(eventStatus: OutboxEventStatus): List<ProductOrderEventOutboxDto>

	fun insert(productOrderEventOutboxDto: ProductOrderEventOutboxDto)

	fun insertAll(productOrderEventOutboxDtos: List<ProductOrderEventOutboxDto>)
}