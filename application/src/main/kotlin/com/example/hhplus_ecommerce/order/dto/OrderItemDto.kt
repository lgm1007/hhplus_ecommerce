package com.example.hhplus_ecommerce.order.dto

import com.example.hhplus_ecommerce.order.OrderItem
import java.time.LocalDateTime

data class OrderItemDto(
	val orderItemId: Long? = null,
	val orderId: Long,
	val productDetailId: Long,
	val quantity: Int,
	val price: Int,
	val createdDate: LocalDateTime? = null
) {
	companion object {
		fun from(orderItem: OrderItem): OrderItemDto {
			return OrderItemDto(
				orderItemId = orderItem.orderItemId,
				orderId = orderItem.orderId,
				productDetailId = orderItem.productDetailId,
				quantity = orderItem.quantity,
				price = orderItem.price,
				createdDate = orderItem.createdDate
			)
		}

		fun fromList(orderItemEntities: List<OrderItem>): List<OrderItemDto> {
			return orderItemEntities.map(::from)
		}

		fun listOf(orderId: Long, orderItemDetailInfoDtos: List<OrderItemDetailInfoDto>): List<OrderItemDto> {
			return orderItemDetailInfoDtos.map { orderItemDetailInfo ->
				OrderItemDto(
					orderItemId = 0,
					orderId = orderId,
					productDetailId = orderItemDetailInfo.productDetailId,
					quantity = orderItemDetailInfo.quantity,
					price = orderItemDetailInfo.price,
					createdDate = LocalDateTime.now()
				)
			}
		}
	}
}