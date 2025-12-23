package com.example.hhplus_ecommerce.order.dto

import com.example.hhplus_ecommerce.order.Order
import com.example.hhplus_ecommerce.order.OrderStatus
import java.time.LocalDateTime

data class OrderDto(
	val orderId: Long? = null,
	val userId: Long,
	val orderDate: LocalDateTime,
	val totalPrice: Int,
	val orderStatus: OrderStatus,
	val createdDate: LocalDateTime? = null,
	val lastModifiedDate: LocalDateTime? = null
) {
	companion object {
		fun from(order: Order): OrderDto {
			return OrderDto(
				orderId = order.orderId,
				userId = order.userId,
				orderDate = order.orderDate,
				totalPrice = order.totalPrice,
				orderStatus = order.orderStatus,
				createdDate = order.createdDate,
				lastModifiedDate = order.lastModifiedDate
			)
		}

		fun from(userId: Long): OrderDto {
			return OrderDto(
				orderId = 0,
				userId = userId,
				orderDate = LocalDateTime.now(),
				totalPrice = 0,
				orderStatus = OrderStatus.ORDER_WAIT,
				createdDate = LocalDateTime.now(),
				lastModifiedDate = LocalDateTime.now()
			)
		}
	}
}