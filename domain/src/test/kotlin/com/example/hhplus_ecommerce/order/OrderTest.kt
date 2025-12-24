package com.example.hhplus_ecommerce.order

import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class OrderTest {
	@Test
	@DisplayName("상품 가격들로 주문 total 가격 더하기 기능 테스트")
	fun addTotalOrderPrice() {
		val order = Order(
			userId = 1L,
			orderDate = LocalDateTime.now(),
			totalPrice = 10000,
			orderStatus = OrderStatus.ORDER_COMPLETE
		)

		order.addTotalPrice(listOf(2000, 3000, 4000))

		assertThat(order.totalPrice).isEqualTo(10000 + 2000 + 3000 + 4000)
	}

	@Test
	@DisplayName("주문 상태 업데이트 기능 테스트")
	fun updateOrderStatus() {
		val order = Order(
			userId = 1L,
			orderDate = LocalDateTime.now(),
			totalPrice = 10000,
			orderStatus = OrderStatus.ORDER_WAIT
		)

		order.updateOrderStatus(OrderStatus.ORDER_COMPLETE)

		assertThat(order.orderStatus).isEqualTo(OrderStatus.ORDER_COMPLETE)
	}
}