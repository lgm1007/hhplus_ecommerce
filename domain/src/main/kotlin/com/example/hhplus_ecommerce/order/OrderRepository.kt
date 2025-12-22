package com.example.hhplus_ecommerce.order

import com.example.hhplus_ecommerce.domain.order.dto.OrderDto
import com.example.hhplus_ecommerce.infrastructure.order.entity.OrderTableEntity

interface OrderRepository {
	fun insert(orderDto: OrderDto): OrderTableEntity

	fun getById(orderId: Long): OrderTableEntity

	fun updateOrderStatus(orderId: Long, orderStatus: OrderStatus): OrderTableEntity
}