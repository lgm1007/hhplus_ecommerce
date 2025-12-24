package com.example.hhplus_ecommerce.order

import com.example.hhplus_ecommerce.order.entity.OrderItemEntity
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class OrderItemRepositoryImpl(private val orderItemJpaRepository: OrderItemJpaRepository) : OrderItemRepository {
	override fun insert(orderItem: OrderItem): OrderItem {
		return orderItemJpaRepository.save(OrderItemEntity.from(orderItem))
			.toDomain()
	}

	override fun insertAll(orderItems: List<OrderItem>): List<OrderItem> {
		val savedEntities = orderItemJpaRepository.saveAll(orderItems.map { OrderItemEntity.from(it) })
		return savedEntities.map { it.toDomain() }
	}

	override fun getAllOrderItemsTopMoreThanDay(day: Int, limit: Int): List<OrderQuantityStatistics> {
		val nowMinusDay = LocalDateTime.now().minusDays(day.toLong())
		return orderItemJpaRepository.findTopQuantityByCreatedDateMoreThan(nowMinusDay, PageRequest.of(0, limit))
	}
}