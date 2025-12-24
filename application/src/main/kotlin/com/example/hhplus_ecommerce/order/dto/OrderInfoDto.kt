package com.example.hhplus_ecommerce.order.dto

import com.example.hhplus_ecommerce.order.OrderStatus
import java.time.LocalDateTime

data class OrderInfoDto(
	val orderId: Long,
	val userId: Long,
	val orderDate: LocalDateTime,
	val totalPrice: Int,
	val status: OrderStatus,
	val orderItemDetailInfos: List<OrderItemDetailInfoDto>
) {
	companion object {
		fun of(orderDto: OrderDto, orderItemDtos: List<OrderItemDto>): OrderInfoDto {
			val orderItemDetailInfoDtos = orderItemDtos.map { itemDto ->
				OrderItemDetailInfoDto.from(itemDto)
			}

			return OrderInfoDto(
				orderDto.orderId!!,
				orderDto.userId,
				orderDto.orderDate,
				orderDto.totalPrice,
				orderDto.orderStatus,
				orderItemDetailInfoDtos
			)
		}
	}
}