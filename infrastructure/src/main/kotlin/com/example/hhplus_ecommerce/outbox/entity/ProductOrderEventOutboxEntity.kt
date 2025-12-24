package com.example.hhplus_ecommerce.outbox.entity

import com.example.hhplus_ecommerce.BaseEntity
import com.example.hhplus_ecommerce.outbox.OutboxEventStatus
import com.example.hhplus_ecommerce.outbox.ProductOrderEventOutbox
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Table

@Entity
@Table(name = "PRODUCTORDEREVENTOUTBOX")
class ProductOrderEventOutboxEntity(
	val userId: Long,
	val productDetailId: Long,
	val orderQuantity: Int,
	@Enumerated(EnumType.STRING)
	var eventStatus: OutboxEventStatus
) : BaseEntity() {
	companion object {
		fun from(productOrderEventOutbox: ProductOrderEventOutbox): ProductOrderEventOutboxEntity {
			return ProductOrderEventOutboxEntity(
				userId = productOrderEventOutbox.userId,
				productDetailId = productOrderEventOutbox.productDetailId,
				orderQuantity = productOrderEventOutbox.orderQuantity,
				eventStatus = productOrderEventOutbox.eventStatus
			)
		}

		fun fromList(productOrderEventOutboxes: List<ProductOrderEventOutbox>): List<ProductOrderEventOutboxEntity> {
			return productOrderEventOutboxes.map(::from)
		}
	}

	fun toDomain(): ProductOrderEventOutbox {
		return ProductOrderEventOutbox(
			productOrderEventOutboxId = this.id,
			userId = this.userId,
			productDetailId = this.productDetailId,
			orderQuantity = this.orderQuantity,
			eventStatus = this.eventStatus,
			createdDate = this.createdDate
		)
	}
}