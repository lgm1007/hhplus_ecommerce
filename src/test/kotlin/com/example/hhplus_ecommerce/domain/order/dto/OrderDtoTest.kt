package com.example.hhplus_ecommerce.domain.order.dto

import com.example.hhplus_ecommerce.domain.order.OrderStatus
import com.example.hhplus_ecommerce.infrastructure.order.entity.Order
import org.assertj.core.api.AssertionsForClassTypes
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class OrderDtoTest {
	@Test
	@DisplayName("주문 Entity로 OrderDto 생성하는 정적 팩토리 메서드 기능 테스트")
	fun orderDtoFromOrder() {
		val order = Order(
			1L,
			LocalDateTime.now(),
			10000,
			OrderStatus.ORDER_WAIT
		)

		val actual = OrderDto.from(order)

		assertThat(actual).isNotNull
		assertThat(actual.userId).isEqualTo(1L)
		assertThat(actual.totalPrice).isEqualTo(10000)
		assertThat(actual.orderStatus).isEqualTo(OrderStatus.ORDER_WAIT)
	}
}