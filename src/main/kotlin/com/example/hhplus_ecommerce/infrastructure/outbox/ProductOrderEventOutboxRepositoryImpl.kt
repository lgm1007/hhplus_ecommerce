package com.example.hhplus_ecommerce.infrastructure.outbox

import com.example.hhplus_ecommerce.domain.outbox.OutboxEventStatus
import com.example.hhplus_ecommerce.domain.outbox.ProductOrderEventOutboxRepository
import com.example.hhplus_ecommerce.domain.outbox.dto.ProductOrderEventOutboxDto
import org.springframework.stereotype.Repository

@Repository
class ProductOrderEventOutboxRepositoryImpl(
	private val jpaRepository: ProductOrderEventOutboxJpaRepository
) : ProductOrderEventOutboxRepository {
	override fun getAllByUserIdAndEventStatus(
		userId: Long,
		eventStatus: OutboxEventStatus
	): List<ProductOrderEventOutboxDto> {
		return ProductOrderEventOutboxDto.fromList(jpaRepository.findAllByUserIdAndEventStatus(userId, eventStatus))
	}
}