package com.example.hhplus_ecommerce.web.order.response

import com.example.hhplus_ecommerce.domain.order.OrderStatus
import com.example.hhplus_ecommerce.domain.order.dto.OrderInfo
import com.example.hhplus_ecommerce.domain.order.dto.OrderItemDetailInfo
import java.time.LocalDateTime

class OrderResponse(
	val orderId: Long,
	val userId: Long,
	val orderDate: LocalDateTime,
	val totalPrice: Int,
	val status: OrderStatus,
	val orderItems: List<OrderItemDetailInfo>
) {
	companion object {
		fun from(orderInfo: OrderInfo): OrderResponse {
			return OrderResponse(
				orderInfo.orderId,
				orderInfo.userId,
				orderInfo.orderDate,
				orderInfo.totalPrice,
				orderInfo.status,
				orderInfo.orderItems
			)
		}
	}
}