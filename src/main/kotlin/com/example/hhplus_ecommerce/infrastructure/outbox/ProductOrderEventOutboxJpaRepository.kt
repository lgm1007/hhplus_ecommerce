package com.example.hhplus_ecommerce.infrastructure.outbox

import com.example.hhplus_ecommerce.domain.outbox.OutboxEventStatus
import com.example.hhplus_ecommerce.infrastructure.outbox.entity.ProductOrderEventOutbox
import org.springframework.data.jpa.repository.JpaRepository

interface ProductOrderEventOutboxJpaRepository : JpaRepository<ProductOrderEventOutbox, Long> {
	fun findAllByEventStatus(eventStatus: OutboxEventStatus): List<ProductOrderEventOutbox>

	fun findAllByUserIdAndProductDetailId(userId: Long, productDetailId: Long): List<ProductOrderEventOutbox>
}