package com.example.hhplus_ecommerce.domain.order

import com.example.hhplus_ecommerce.domain.order.dto.OrderDto
import com.example.hhplus_ecommerce.domain.order.dto.OrderItemDto
import com.example.hhplus_ecommerce.domain.order.dto.OrderQuantityStatisticsInfo
import org.springframework.stereotype.Service

@Service
class OrderService(
	private val orderRepository: OrderRepository,
	private val orderItemRepository: OrderItemRepository
) {
	fun registerOrder(orderDto: OrderDto): OrderDto {
		return OrderDto.from(orderRepository.insert(orderDto))
	}

	fun registerOrderItems(orderItemDtos: List<OrderItemDto>): List<OrderItemDto> {
		return orderItemRepository.insertAll(orderItemDtos).map { orderItem ->
			OrderItemDto.from(orderItem)
		}
	}

	fun getOrderById(orderId: Long): OrderDto {
		return OrderDto.from(orderRepository.getById(orderId))
	}

	fun updateOrderStatus(orderId: Long, orderStatus: OrderStatus): OrderDto {
		return OrderDto.from(orderRepository.updateOrderStatus(orderId, orderStatus))
	}

	fun getAllOrderItemsTopMoreThanDay(day: Int, limit: Int): List<OrderQuantityStatisticsInfo> {
		return orderItemRepository.getAllOrderItemsTopMoreThanDay(day, limit)
	}
}