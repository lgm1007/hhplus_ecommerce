package com.example.hhplus_ecommerce.domain.outbox

import com.example.hhplus_ecommerce.domain.outbox.dto.ProductOrderEventOutboxDto
import com.example.hhplus_ecommerce.domain.outbox.dto.ProductOrderEventOutboxRequestDto

interface ProductOrderEventOutboxRepository {
	fun getAllByEventStatus(eventStatus: OutboxEventStatus): List<ProductOrderEventOutboxDto>

	fun getAllByUserIdAndProductDetailId(userId: Long, productDetailId: Long): List<ProductOrderEventOutboxDto>

	fun insert(productOrderEventOutboxDto: ProductOrderEventOutboxDto)

	fun insertAll(productOrderEventOutboxDtos: List<ProductOrderEventOutboxDto>)

	fun updateStatus(outboxRequestDto: ProductOrderEventOutboxRequestDto, eventStatus: OutboxEventStatus): ProductOrderEventOutboxDto

	fun deleteAll()
}