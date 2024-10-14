package com.example.hhplus_ecommerce.domain.order.dto

import com.example.hhplus_ecommerce.domain.order.OrderStatus
import com.example.hhplus_ecommerce.infrastructure.order.entity.Order
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
	fun addTotalPrice(itemPrice: Int) {
		this.totalPrice += itemPrice
	}

	fun updateOrderStatus(orderStatus: OrderStatus) {
		this.orderStatus = orderStatus
	}

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