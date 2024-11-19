package com.example.hhplus_ecommerce.infrastructure.outbox

import com.example.hhplus_ecommerce.domain.outbox.OutboxEventStatus
import com.example.hhplus_ecommerce.domain.outbox.ProductOrderEventOutboxRepository
import com.example.hhplus_ecommerce.domain.outbox.dto.ProductOrderEventOutboxDto
import com.example.hhplus_ecommerce.infrastructure.outbox.entity.ProductOrderEventOutbox
import org.springframework.stereotype.Repository

@Repository
class ProductOrderEventOutboxRepositoryImpl(
	private val jpaRepository: ProductOrderEventOutboxJpaRepository
) : ProductOrderEventOutboxRepository {
	override fun getAllByEventStatus(
		eventStatus: OutboxEventStatus
	): List<ProductOrderEventOutboxDto> {
		return ProductOrderEventOutboxDto.fromList(jpaRepository.findAllByEventStatus(eventStatus))
	}

	override fun insert(productOrderEventOutboxDto: ProductOrderEventOutboxDto) {
		jpaRepository.save(ProductOrderEventOutbox.from(productOrderEventOutboxDto))
	}

	override fun insertAll(productOrderEventOutboxDtos: List<ProductOrderEventOutboxDto>) {
		jpaRepository.saveAll(ProductOrderEventOutbox.fromList(productOrderEventOutboxDtos))
	}
}