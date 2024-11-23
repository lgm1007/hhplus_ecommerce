package com.example.hhplus_ecommerce.domain.outbox.dto

import com.example.hhplus_ecommerce.domain.outbox.OutboxEventStatus
import com.example.hhplus_ecommerce.infrastructure.outbox.entity.ProductOrderEventOutbox
import java.time.LocalDateTime

data class ProductOrderEventOutboxDto(
	val id: Long,
	val userId: Long,
	val productDetailId: Long,
	val orderQuantity: Int,
	val eventStatus: OutboxEventStatus,
	val createdDate: LocalDateTime
) {
	companion object {
		fun from(productOrderEventOutbox: ProductOrderEventOutbox): ProductOrderEventOutboxDto {
			return ProductOrderEventOutboxDto(
				productOrderEventOutbox.id,
				productOrderEventOutbox.userId,
				productOrderEventOutbox.productDetailId,
				productOrderEventOutbox.orderQuantity,
				productOrderEventOutbox.eventStatus,
				productOrderEventOutbox.createdDate
			)
		}

		fun fromList(productOrderEventOutboxs: List<ProductOrderEventOutbox>): List<ProductOrderEventOutboxDto> {
			return productOrderEventOutboxs.map(::from)
		}
	}
}