package com.example.hhplus_ecommerce.domain.order.dto

import com.example.hhplus_ecommerce.infrastructure.order.entity.OrderItem
import java.time.LocalDateTime

class OrderItemDto(
	val id: Long,
	val orderId: Long,
	val productDetailId: Long,
	val quantity: Int,
	val price: Int,
	val createdDate: LocalDateTime
) {
	companion object {
		fun from(orderItem: OrderItem): OrderItemDto {
			return OrderItemDto(
				orderItem.id,
				orderItem.orderId,
				orderItem.productDetailId,
				orderItem.quantity,
				orderItem.price,
				orderItem.createdDate
			)
		}
	}
}