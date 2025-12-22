package com.example.hhplus_ecommerce.order

import java.time.LocalDateTime

data class Order(
	val orderId: Long? = null,
	val userId: Long,
	val orderDate: LocalDateTime,
	var totalPrice: Int,
	var orderStatus: OrderStatus,
	var createdDate: LocalDateTime? = null,
	var lastModifiedDate: LocalDateTime? = null
) {
	/**
	 * 주문 가격 목록 더하기
	 */
	fun addTotalPrice(prices: List<Int>) {
		prices.forEach { price ->
			this.totalPrice += price
		}
	}

	/**
	 * 주문 상태 업데이트
	 */
	fun updateOrderStatus(orderStatus: OrderStatus) {
		this.orderStatus = orderStatus
	}
}
