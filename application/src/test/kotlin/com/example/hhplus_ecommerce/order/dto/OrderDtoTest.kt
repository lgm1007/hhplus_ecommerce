package com.example.hhplus_ecommerce.order.dto

import com.example.hhplus_ecommerce.order.Order
import com.example.hhplus_ecommerce.order.OrderStatus
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class OrderDtoTest {
	@Test
	@DisplayName("주문 도메인 모델로 OrderDto 생성하는 정적 팩토리 메서드 기능 테스트")
	fun orderDtoFromOrder() {
		val order = Order(
			userId = 1L,
			orderDate = LocalDateTime.now(),
			totalPrice = 10000,
			orderStatus = OrderStatus.ORDER_WAIT
		)

		val actual = OrderDto.from(order)

		assertThat(actual).isNotNull
		assertThat(actual.userId).isEqualTo(1L)
		assertThat(actual.totalPrice).isEqualTo(10000)
		assertThat(actual.orderStatus).isEqualTo(OrderStatus.ORDER_WAIT)
	}
}