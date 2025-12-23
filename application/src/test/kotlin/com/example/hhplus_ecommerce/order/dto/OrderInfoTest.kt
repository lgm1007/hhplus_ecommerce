package com.example.hhplus_ecommerce.order.dto

import com.example.hhplus_ecommerce.order.OrderStatus
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class OrderInfoTest {
	@Test
	@DisplayName("OrderDto와 OrderItemDto 목록으로 주문 응답 정보인 OrderInfo 생성 정적 팩토리 메서드 기능 테스트")
	fun orderInfoOfDto() {
		val orderDto = OrderDto(
			orderId = 1L,
			userId = 123L,
			orderDate = LocalDateTime.now(),
			totalPrice = 10000,
			orderStatus = OrderStatus.ORDER_COMPLETE,
			createdDate = LocalDateTime.now(),
			lastModifiedDate = LocalDateTime.now()
		)
		val orderItemDtos = listOf(
			OrderItemDto(
				orderItemId = 1L,
				orderId = 1L,
				productDetailId = 987L,
				quantity = 50,
				price = 7000,
				createdDate = LocalDateTime.now()
			),
			OrderItemDto(
				orderItemId = 2L,
				orderId = 1L,
				productDetailId = 654L,
				quantity = 30,
				price = 3000,
				createdDate = LocalDateTime.now()
			)
		)

		val actual = OrderInfoDto.of(orderDto, orderItemDtos)

		assertThat(actual).isNotNull
		assertThat(actual.orderId).isEqualTo(1L)
		assertThat(actual.userId).isEqualTo(123L)
		assertThat(actual.totalPrice).isEqualTo(10000)
		assertThat(actual.status).isEqualTo(OrderStatus.ORDER_COMPLETE)
		assertThat(actual.orderItems.size).isEqualTo(2)
	}
}