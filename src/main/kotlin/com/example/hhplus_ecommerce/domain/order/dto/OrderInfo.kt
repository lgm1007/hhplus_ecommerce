package com.example.hhplus_ecommerce.domain.order.dto

import com.example.hhplus_ecommerce.domain.order.OrderStatus
import java.time.LocalDateTime

class OrderInfo(
	val orderId: Long,
	val userId: Long,
	val orderDate: LocalDateTime,
	val totalPrice: Int,
	val status: OrderStatus,
	val orderItems: List<OrderItemDetailInfo>
) {
	companion object {
		fun of(orderDto: OrderDto, orderItemDtos: List<OrderItemDto>): OrderInfo {
			val orderItemDetailInfos = orderItemDtos.map { itemDto ->
				OrderItemDetailInfo.from(itemDto)
			}

			return OrderInfo(
				orderDto.id,
				orderDto.userId,
				orderDto.orderDate,
				orderDto.totalPrice,
				orderDto.orderStatus,
				orderItemDetailInfos
			)
		}
	}
}