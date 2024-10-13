package com.example.hhplus_ecommerce.domain.order.dto

import com.example.hhplus_ecommerce.domain.order.OrderStatus
import com.example.hhplus_ecommerce.infrastructure.order.entity.Order
import java.time.LocalDateTime

class OrderDto(
	val id: Long,
	val userId: Long,
	val orderDate: LocalDateTime,
	val totalPrice: Int,
	val orderStatus: OrderStatus,
	val createdDate: LocalDateTime,
	val lastModifiedDate: LocalDateTime
) {
	companion object {
		fun from(order: Order): OrderDto {
			return OrderDto(
				order.id,
				order.userId,
				order.orderDate,
				order.totalPrice,
				order.orderStatus,
				order.createdDate,
				order.lastModifiedDate
			)
		}
	}
}