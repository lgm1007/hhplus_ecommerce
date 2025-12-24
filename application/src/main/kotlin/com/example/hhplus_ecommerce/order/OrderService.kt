package com.example.hhplus_ecommerce.order

import com.example.hhplus_ecommerce.order.dto.OrderDto
import com.example.hhplus_ecommerce.order.dto.OrderItemDetailInfoDto
import com.example.hhplus_ecommerce.order.dto.OrderItemDto
import com.example.hhplus_ecommerce.order.dto.OrderQuantityStatisticsInfoDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

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
		orderItemDetailInfoDtos: List<OrderItemDetailInfoDto>
	): Pair<OrderDto, List<OrderItemDto>> {
		val totalPrices = orderItemDetailInfoDtos.map { orderItemDetailInfoDto ->
			val orderItemDetailInfo = OrderItemDetailInfo(
				productDetailId = orderItemDetailInfoDto.productDetailId,
				quantity = orderItemDetailInfoDto.quantity,
				price = orderItemDetailInfoDto.price
			)
			orderItemDetailInfo.calculateOrderPrice()
		}
		val order = Order(
			userId = userId,
			orderDate = LocalDateTime.now(),
			totalPrice = 0,
			orderStatus = OrderStatus.ORDER_WAIT
		)

		order.addTotalPrice(totalPrices)
		order.updateOrderStatus(OrderStatus.ORDER_COMPLETE)
		val savedOrder = insertOrder(OrderDto.from(order))

		val orderItemDtos = OrderItemDto.listOf(savedOrder.orderId!!, orderItemDetailInfoDtos)
		val savedOrderItems = insertAllOrderItems(orderItemDtos)
		return Pair(savedOrder, savedOrderItems)
	}

	fun insertOrder(orderDto: OrderDto): OrderDto {
		return OrderDto.from(
			orderRepository.insert(
				order = Order(
					userId = orderDto.userId,
					totalPrice = orderDto.totalPrice,
					orderStatus = orderDto.orderStatus,
					orderDate = orderDto.orderDate
				)
			)
		)
	}

	fun insertAllOrderItems(orderItemDtos: List<OrderItemDto>): List<OrderItemDto> {
		return orderItemRepository.insertAll(
			orderItems = orderItemDtos.map {
				OrderItem(
					orderId = it.orderId,
					productDetailId = it.productDetailId,
					quantity = it.quantity,
					price = it.price
				)
			}
		).map { orderItem ->
			OrderItemDto.from(orderItem)
		}
	}

	fun getOrderById(orderId: Long): OrderDto {
		return OrderDto.from(orderRepository.getById(orderId))
	}

	fun updateOrderStatus(orderId: Long, orderStatus: OrderStatus): OrderDto {
		return OrderDto.from(orderRepository.updateOrderStatus(orderId, orderStatus))
	}

	fun getAllOrderItemsTopMoreThanDay(day: Int, limit: Int): List<OrderQuantityStatisticsInfoDto> {
		return orderItemRepository.getAllOrderItemsTopMoreThanDay(day, limit).map {
			OrderQuantityStatisticsInfoDto(
				productDetailId = it.productDetailId,
				sumQuantity = it.sumQuantity
			)
		}
	}
}