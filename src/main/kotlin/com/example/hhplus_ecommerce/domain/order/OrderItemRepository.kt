package com.example.hhplus_ecommerce.domain.order

import com.example.hhplus_ecommerce.domain.order.dto.OrderItemDto
import com.example.hhplus_ecommerce.infrastructure.order.entity.OrderItem

interface OrderItemRepository {
	fun insert(orderItemDto: OrderItemDto): OrderItem

	fun insertAll(orderItemDtos: List<OrderItemDto>): List<OrderItem>
}