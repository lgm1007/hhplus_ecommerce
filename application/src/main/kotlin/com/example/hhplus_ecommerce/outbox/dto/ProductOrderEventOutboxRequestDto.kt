package com.example.hhplus_ecommerce.outbox.dto

import com.example.hhplus_ecommerce.outbox.OutboxEventStatus

data class ProductOrderEventOutboxRequestDto(
	val userId: Long,
	val productDetailId: Long,
	val orderQuantity: Int,
	val eventStatus: OutboxEventStatus
)
