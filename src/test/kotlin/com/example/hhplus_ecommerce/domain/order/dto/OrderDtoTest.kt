package com.example.hhplus_ecommerce.domain.order.dto

import com.example.hhplus_ecommerce.domain.order.OrderStatus
import com.example.hhplus_ecommerce.infrastructure.order.entity.Order
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

	@Test
	@DisplayName("주문 아이템 목록의 상품 가격들로 주문 가격을 더하는 기능 테스트")
	fun addTotalOrderPrice() {
		val orderDto = OrderDto(
			1L,
			1L,
			LocalDateTime.now(),
			10000,
			OrderStatus.ORDER_COMPLETE,
			LocalDateTime.now(),
			LocalDateTime.now(),
		)

		orderDto.addTotalPrice(listOf(2000, 3000, 4000))

		assertThat(orderDto.totalPrice).isEqualTo(19000)
	}

	@Test
	@DisplayName("주문 상태 업데이트 기능 테스트")
	fun updateOrderStatus() {
		val orderDto = OrderDto(
			1L,
			1L,
			LocalDateTime.now(),
			10000,
			OrderStatus.ORDER_WAIT,
			LocalDateTime.now(),
			LocalDateTime.now(),
		)

		orderDto.updateOrderStatus(OrderStatus.ORDER_COMPLETE)

		assertThat(orderDto.orderStatus).isEqualTo(OrderStatus.ORDER_COMPLETE)
	}
}