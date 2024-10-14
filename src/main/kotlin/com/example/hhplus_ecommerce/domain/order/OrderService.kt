package com.example.hhplus_ecommerce.domain.order

import com.example.hhplus_ecommerce.domain.order.dto.OrderDto
import com.example.hhplus_ecommerce.domain.order.dto.OrderItemDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
	private val orderRepository: OrderRepository,
	private val orderItemRepository: OrderItemRepository
) {
	@Transactional
	fun registerOrder(orderDto: OrderDto): OrderDto {
		return orderRepository.insert(orderDto)
	}

	@Transactional
	fun registerOrderItems(orderItemDtos: List<OrderItemDto>): List<OrderItemDto> {
		return orderItemRepository.insertAll(orderItemDtos)
	}

}