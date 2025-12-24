package com.example.hhplus_ecommerce.order.response

import com.example.hhplus_ecommerce.order.OrderStatus
import com.example.hhplus_ecommerce.order.dto.OrderInfoDto
import com.example.hhplus_ecommerce.order.dto.OrderItemDetailInfoDto
import java.time.LocalDateTime

data class OrderResponse(
	val orderId: Long,
	val userId: Long,
	val orderDate: LocalDateTime,
	val totalPrice: Int,
	val status: OrderStatus,
	val orderItemDetailInfos: List<OrderItemDetailInfoDto>
) {
	companion object {
		fun from(orderInfo: OrderInfoDto): OrderResponse {
			return OrderResponse(
				orderId = orderInfo.orderId,
				userId = orderInfo.userId,
				orderDate = orderInfo.orderDate,
				totalPrice = orderInfo.totalPrice,
				status = orderInfo.status,
				orderItemDetailInfos = orderInfo.orderItemDetailInfos
			)
		}
	}
}