package com.example.hhplus_ecommerce.domain.order.dto

import com.example.hhplus_ecommerce.domain.order.OrderStatus
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class OrderInfoTest {
	@Test
	@DisplayName("OrderDto와 OrderItemDto 목록으로 주문 응답 정보인 OrderInfo 생성 정적 팩토리 메서드 기능 테스트")
	fun orderInfoOfDto() {
		val orderDto = OrderDto(
			1L,
			123L,
			LocalDateTime.now(),
			10000,
			OrderStatus.ORDER_COMPLETE,
			LocalDateTime.now(),
			LocalDateTime.now()
		)
		val orderItemDtos = listOf(
			OrderItemDto(1L, 1L, 987L, 50, 7000, LocalDateTime.now()),
			OrderItemDto(2L, 1L, 654L, 30, 3000, LocalDateTime.now()),
		)

		val actual = OrderInfo.of(orderDto, orderItemDtos)

		assertThat(actual).isNotNull
		assertThat(actual.orderId).isEqualTo(1L)
		assertThat(actual.userId).isEqualTo(123L)
		assertThat(actual.totalPrice).isEqualTo(10000)
		assertThat(actual.status).isEqualTo(OrderStatus.ORDER_COMPLETE)
		assertThat(actual.orderItems.size).isEqualTo(2)
	}
}