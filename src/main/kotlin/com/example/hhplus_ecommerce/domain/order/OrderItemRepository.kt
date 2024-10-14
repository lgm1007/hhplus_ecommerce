package com.example.hhplus_ecommerce.domain.order

import com.example.hhplus_ecommerce.domain.order.dto.OrderItemDto

interface OrderItemRepository {
	fun insert(orderItemDto: OrderItemDto): OrderItemDto

	fun insertAll(orderItemDtos: List<OrderItemDto>): List<OrderItemDto>
}