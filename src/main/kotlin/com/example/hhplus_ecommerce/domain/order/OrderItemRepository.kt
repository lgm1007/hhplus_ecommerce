package com.example.hhplus_ecommerce.domain.order

import com.example.hhplus_ecommerce.domain.order.dto.OrderItemDto
import com.example.hhplus_ecommerce.domain.order.dto.OrderQuantityStatisticsInfo
import com.example.hhplus_ecommerce.infrastructure.order.entity.OrderItemEntity

interface OrderItemRepository {
	fun insert(orderItemDto: OrderItemDto): OrderItemEntity

	fun insertAll(orderItemDtos: List<OrderItemDto>): List<OrderItemEntity>

	fun getAllOrderItemsTopMoreThanDay(day: Int, limit: Int): List<OrderQuantityStatisticsInfo>
}