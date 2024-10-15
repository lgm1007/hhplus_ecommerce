package com.example.hhplus_ecommerce.domain.order

import com.example.hhplus_ecommerce.domain.order.dto.OrderDto
import com.example.hhplus_ecommerce.infrastructure.order.entity.Order

interface OrderRepository {
	fun insert(orderDto: OrderDto): Order

	fun getById(orderId: Long): Order

	fun updateOrderStatus(orderId: Long, orderStatus: OrderStatus): Order
}