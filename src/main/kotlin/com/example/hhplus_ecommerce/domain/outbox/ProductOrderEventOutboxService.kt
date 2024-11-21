package com.example.hhplus_ecommerce.domain.outbox

import com.example.hhplus_ecommerce.domain.outbox.dto.ProductOrderEventOutboxDto
import com.example.hhplus_ecommerce.domain.outbox.dto.ProductOrderEventOutboxRequestDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductOrderEventOutboxService(
	private val productOrderEventOutboxRepository: ProductOrderEventOutboxRepository
) {
	fun getAllByEventStatus(eventStatus: OutboxEventStatus): List<ProductOrderEventOutboxDto> {
		return productOrderEventOutboxRepository.getAllByEventStatus(eventStatus)
	}

	fun save(outbox: ProductOrderEventOutboxDto) {
		productOrderEventOutboxRepository.insert(outbox)
	}

	fun saveAll(outboxList: List<ProductOrderEventOutboxDto>) {
		productOrderEventOutboxRepository.insertAll(outboxList)
	}

	@Transactional
	fun updateEventStatusPublish(outboxRequestDto: ProductOrderEventOutboxRequestDto): ProductOrderEventOutboxDto {
		return productOrderEventOutboxRepository.updateStatus(outboxRequestDto, OutboxEventStatus.PUBLISH)
	}

	@Transactional
	fun updateEventStatusComplete(outboxRequestDto: ProductOrderEventOutboxRequestDto): ProductOrderEventOutboxDto {
		return productOrderEventOutboxRepository.updateStatus(outboxRequestDto, OutboxEventStatus.COMPLETE)
	}

	@Transactional
	fun deleteById(outboxId: Long) {
		productOrderEventOutboxRepository.deleteById(outboxId)
	}
}