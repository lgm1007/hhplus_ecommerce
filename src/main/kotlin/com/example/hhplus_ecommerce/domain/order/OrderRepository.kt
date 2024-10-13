package com.example.hhplus_ecommerce.domain.order

import com.example.hhplus_ecommerce.domain.order.dto.OrderDto

interface OrderRepository {
	fun insert(orderDto: OrderDto): OrderDto
}