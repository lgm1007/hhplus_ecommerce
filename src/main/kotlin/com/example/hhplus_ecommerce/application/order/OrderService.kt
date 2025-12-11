package com.example.hhplus_ecommerce.application.order

import com.example.hhplus_ecommerce.domain.order.OrderItemRepository
import com.example.hhplus_ecommerce.domain.order.OrderRepository
import com.example.hhplus_ecommerce.domain.order.OrderStatus
import com.example.hhplus_ecommerce.domain.order.dto.OrderDto
import com.example.hhplus_ecommerce.domain.order.dto.OrderItemDetailInfo
import com.example.hhplus_ecommerce.domain.order.dto.OrderItemDto
import com.example.hhplus_ecommerce.domain.order.dto.OrderQuantityStatisticsInfo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
	private val orderRepository: OrderRepository,
	private val orderItemRepository: OrderItemRepository
) {
	/**
	 * 주문 정보 저장
	 */
	@Transactional
	fun doOrder(
		userId: Long,
		orderItemDetailInfos: List<OrderItemDetailInfo>
	): Pair<OrderDto, List<OrderItemDto>> {
		val totalPrices = orderItemDetailInfos.map { orderItemDetailInfo -> orderItemDetailInfo.calculateOrderPrice() }
		val orderDto = OrderDto.Companion.from(userId)

		orderDto.addTotalPrice(totalPrices)
		orderDto.updateOrderStatus(OrderStatus.ORDER_COMPLETE)
		val savedOrder = insertOrder(orderDto)

		val orderItemDtos = OrderItemDto.Companion.listOf(savedOrder.id, orderItemDetailInfos)
		val savedOrderItems = insertAllOrderItems(orderItemDtos)
		return Pair(savedOrder, savedOrderItems)
	}

	fun insertOrder(orderDto: OrderDto): OrderDto {
		return OrderDto.Companion.from(orderRepository.insert(orderDto))
	}

	fun insertAllOrderItems(orderItemDtos: List<OrderItemDto>): List<OrderItemDto> {
		return orderItemRepository.insertAll(orderItemDtos).map { orderItem ->
			OrderItemDto.Companion.from(orderItem)
		}
	}

	fun getOrderById(orderId: Long): OrderDto {
		return OrderDto.Companion.from(orderRepository.getById(orderId))
	}

	fun updateOrderStatus(orderId: Long, orderStatus: OrderStatus): OrderDto {
		return OrderDto.Companion.from(orderRepository.updateOrderStatus(orderId, orderStatus))
	}

	fun getAllOrderItemsTopMoreThanDay(day: Int, limit: Int): List<OrderQuantityStatisticsInfo> {
		return orderItemRepository.getAllOrderItemsTopMoreThanDay(day, limit)
	}
}