package com.example.hhplus_ecommerce.infrastructure.outbox

import com.example.hhplus_ecommerce.domain.outbox.OutboxEventStatus
import com.example.hhplus_ecommerce.domain.outbox.ProductOrderEventOutboxRepository
import com.example.hhplus_ecommerce.domain.outbox.dto.ProductOrderEventOutboxDto
import com.example.hhplus_ecommerce.domain.outbox.dto.ProductOrderEventOutboxRequestDto
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

	override fun getAllByUserIdAndProductDetailId(
		userId: Long,
		productDetailId: Long
	): List<ProductOrderEventOutboxDto> {
		return ProductOrderEventOutboxDto.fromList(jpaRepository.findAllByUserIdAndProductDetailId(userId, productDetailId))
	}

	override fun insert(productOrderEventOutboxDto: ProductOrderEventOutboxDto) {
		jpaRepository.save(ProductOrderEventOutbox.from(productOrderEventOutboxDto))
	}

	override fun insertAll(productOrderEventOutboxDtos: List<ProductOrderEventOutboxDto>) {
		jpaRepository.saveAll(ProductOrderEventOutbox.fromList(productOrderEventOutboxDtos))
	}

	override fun updateStatus(
		outboxRequestDto: ProductOrderEventOutboxRequestDto,
		eventStatus: OutboxEventStatus
	): ProductOrderEventOutboxDto {
		val orderEventOutbox = jpaRepository.findAllByUserIdAndProductDetailId(
			outboxRequestDto.userId, outboxRequestDto.productDetailId
		)
		.filter { it.orderQuantity == outboxRequestDto.orderQuantity && it.eventStatus != eventStatus }
		.sortedByDescending { it.createdDate }
		.first()

		orderEventOutbox.modifyEventStatus(eventStatus)

		return ProductOrderEventOutboxDto.from(orderEventOutbox)
	}

	override fun deleteAll() {
		jpaRepository.deleteAll()
	}
}