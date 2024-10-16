package com.example.hhplus_ecommerce.infrastructure.order

import com.example.hhplus_ecommerce.domain.order.OrderItemRepository
import com.example.hhplus_ecommerce.domain.order.dto.OrderItemDto
import com.example.hhplus_ecommerce.domain.order.dto.OrderQuantityStatisticsInfo
import com.example.hhplus_ecommerce.infrastructure.order.entity.OrderItem
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class OrderItemRepositoryImpl(private val orderItemJpaRepository: OrderItemJpaRepository) : OrderItemRepository {
	override fun insert(orderItemDto: OrderItemDto): OrderItem {
		return orderItemJpaRepository.save(OrderItem.from(orderItemDto))
	}

	override fun insertAll(orderItemDtos: List<OrderItemDto>): List<OrderItem> {
		return orderItemJpaRepository.saveAll(orderItemDtos.map { orderItemDto ->
				OrderItem.from(orderItemDto)
			})
	}

	override fun getAllOrderItemsTopMoreThanDay(day: Int, limit: Int): List<OrderQuantityStatisticsInfo> {
		val nowMinusDay = LocalDateTime.now().minusDays(day.toLong())
		return orderItemJpaRepository.findTopQuantityByCreatedDateMoreThan(nowMinusDay, PageRequest.of(0, limit))
	}
}