package com.example.hhplus_ecommerce.domain.outbox.dto

import com.example.hhplus_ecommerce.domain.outbox.OutboxEventStatus
import com.example.hhplus_ecommerce.infrastructure.outbox.entity.ProductOrderEventOutboxEntity
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
		fun from(productOrderEventOutboxEntity: ProductOrderEventOutboxEntity): ProductOrderEventOutboxDto {
			return ProductOrderEventOutboxDto(
				productOrderEventOutboxEntity.id,
				productOrderEventOutboxEntity.userId,
				productOrderEventOutboxEntity.productDetailId,
				productOrderEventOutboxEntity.orderQuantity,
				productOrderEventOutboxEntity.eventStatus,
				productOrderEventOutboxEntity.createdDate
			)
		}

		fun fromList(productOrderEventOutboxEntities: List<ProductOrderEventOutboxEntity>): List<ProductOrderEventOutboxDto> {
			return productOrderEventOutboxEntities.map(::from)
		}
	}
}