package com.example.hhplus_ecommerce.outbox.dto

import com.example.hhplus_ecommerce.outbox.OutboxEventStatus
import com.example.hhplus_ecommerce.outbox.ProductOrderEventOutbox
import java.time.LocalDateTime

data class ProductOrderEventOutboxDto(
	val productOrderEventOutboxId: Long? = null,
	val userId: Long,
	val productDetailId: Long,
	val orderQuantity: Int,
	val eventStatus: OutboxEventStatus,
	val createdDate: LocalDateTime? = null
) {
	companion object {
		fun from(productOrderEventOutbox: ProductOrderEventOutbox): ProductOrderEventOutboxDto {
			return ProductOrderEventOutboxDto(
				productOrderEventOutboxId = productOrderEventOutbox.productOrderEventOutboxId,
				userId = productOrderEventOutbox.userId,
				productDetailId = productOrderEventOutbox.productDetailId,
				orderQuantity = productOrderEventOutbox.orderQuantity,
				eventStatus = productOrderEventOutbox.eventStatus,
				createdDate = productOrderEventOutbox.createdDate
			)
		}

		fun fromList(productOrderEventOutboxEntities: List<ProductOrderEventOutbox>): List<ProductOrderEventOutboxDto> {
			return productOrderEventOutboxEntities.map(::from)
		}
	}
}