package com.example.hhplus_ecommerce.infrastructure.outbox.entity

import com.example.hhplus_ecommerce.domain.outbox.OutboxEventStatus
import com.example.hhplus_ecommerce.domain.outbox.dto.ProductOrderEventOutboxDto
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
class ProductOrderEventOutbox(
	val userId: Long,
	val productDetailId: Long,
	val orderQuantity: Int,
	@Enumerated(EnumType.STRING)
	var eventStatus: OutboxEventStatus
) {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0

	@CreatedDate
	var createdDate: LocalDateTime = LocalDateTime.now()
		private set

	fun modifyEventStatus(eventStatus: OutboxEventStatus) {
		this.eventStatus = eventStatus
	}

	companion object {
		fun from(productOrderEventOutboxDto: ProductOrderEventOutboxDto): ProductOrderEventOutbox {
			return ProductOrderEventOutbox(
				userId = productOrderEventOutboxDto.userId,
				productDetailId = productOrderEventOutboxDto.productDetailId,
				orderQuantity = productOrderEventOutboxDto.orderQuantity,
				eventStatus = productOrderEventOutboxDto.eventStatus
			)
		}

		fun fromList(productOrderEventOutboxDtos: List<ProductOrderEventOutboxDto>): List<ProductOrderEventOutbox> {
			return productOrderEventOutboxDtos.map(::from)
		}
	}
}