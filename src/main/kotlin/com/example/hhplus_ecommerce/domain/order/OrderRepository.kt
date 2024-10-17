package com.example.hhplus_ecommerce.domain.order

import com.example.hhplus_ecommerce.domain.order.dto.OrderDto
import com.example.hhplus_ecommerce.infrastructure.order.entity.OrderTable

interface OrderRepository {
	fun insert(orderDto: OrderDto): OrderTable

	fun getById(orderId: Long): OrderTable

	fun updateOrderStatus(orderId: Long, orderStatus: OrderStatus): OrderTable
}