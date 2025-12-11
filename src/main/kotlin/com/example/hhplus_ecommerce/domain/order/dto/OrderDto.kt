package com.example.hhplus_ecommerce.domain.order.dto

import com.example.hhplus_ecommerce.domain.order.OrderStatus
import com.example.hhplus_ecommerce.infrastructure.order.entity.OrderTableEntity
import java.time.LocalDateTime

class OrderDto(
	val id: Long,
	val userId: Long,
	val orderDate: LocalDateTime,
	var totalPrice: Int,
	var orderStatus: OrderStatus,
	val createdDate: LocalDateTime,
	val lastModifiedDate: LocalDateTime
) {
	fun addTotalPrice(prices: List<Int>) {
		prices.forEach {price ->
			this.totalPrice += price
		}
	}

	fun updateOrderStatus(orderStatus: OrderStatus) {
		this.orderStatus = orderStatus
	}

	companion object {
		fun from(order: OrderTableEntity): OrderDto {
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

		fun from(userId: Long): OrderDto {
			return OrderDto(
				0,
				userId,
				LocalDateTime.now(),
				0,
				OrderStatus.ORDER_WAIT,
				LocalDateTime.now(),
				LocalDateTime.now(),
			)
		}
	}
}