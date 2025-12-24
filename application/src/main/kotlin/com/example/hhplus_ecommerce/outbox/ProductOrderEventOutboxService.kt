package com.example.hhplus_ecommerce.outbox

import com.example.hhplus_ecommerce.outbox.dto.ProductOrderEventOutboxDto
import com.example.hhplus_ecommerce.outbox.dto.ProductOrderEventOutboxRequestDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductOrderEventOutboxService(
	private val productOrderEventOutboxRepository: ProductOrderEventOutboxRepository
) {
	fun getAllByEventStatus(eventStatus: OutboxEventStatus): List<ProductOrderEventOutboxDto> {
		return productOrderEventOutboxRepository.getAllByEventStatus(eventStatus).map { ProductOrderEventOutboxDto.from(it) }
	}

	fun save(outbox: ProductOrderEventOutboxDto) {
		productOrderEventOutboxRepository.insert(
			ProductOrderEventOutbox(
				userId = outbox.userId,
				productDetailId = outbox.productDetailId,
				orderQuantity = outbox.orderQuantity,
				eventStatus = outbox.eventStatus
			)
		)
	}

	fun saveAll(outboxList: List<ProductOrderEventOutboxDto>) {
		productOrderEventOutboxRepository.insertAll(
			productOrderEventOutboxes = outboxList.map {
				ProductOrderEventOutbox(
					userId = it.userId,
					productDetailId = it.productDetailId,
					orderQuantity = it.orderQuantity,
					eventStatus = it.eventStatus
				)
			}
		)
	}

	@Transactional
	fun updateEventStatus(requestDto: ProductOrderEventOutboxRequestDto): ProductOrderEventOutboxDto {
		return productOrderEventOutboxRepository.updateStatus(
			ProductOrderEventOutboxPatch(
				userId = requestDto.userId,
				productDetailId = requestDto.productDetailId,
				orderQuantity = requestDto.orderQuantity,
				eventStatus = requestDto.eventStatus
			)
		).let { ProductOrderEventOutboxDto.from(it) }
	}

	@Transactional
	fun deleteById(outboxId: Long) {
		productOrderEventOutboxRepository.deleteById(outboxId)
	}
}