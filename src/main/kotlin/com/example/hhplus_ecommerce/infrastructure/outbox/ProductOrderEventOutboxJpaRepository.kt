package com.example.hhplus_ecommerce.infrastructure.outbox

import com.example.hhplus_ecommerce.domain.outbox.OutboxEventStatus
import com.example.hhplus_ecommerce.infrastructure.outbox.entity.ProductOrderEventOutboxEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ProductOrderEventOutboxJpaRepository : JpaRepository<ProductOrderEventOutboxEntity, Long> {
	fun findAllByEventStatus(eventStatus: OutboxEventStatus): List<ProductOrderEventOutboxEntity>

	fun findAllByUserIdAndProductDetailId(userId: Long, productDetailId: Long): List<ProductOrderEventOutboxEntity>
}