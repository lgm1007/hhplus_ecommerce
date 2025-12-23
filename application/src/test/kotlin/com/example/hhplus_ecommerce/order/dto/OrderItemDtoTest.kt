package com.example.hhplus_ecommerce.order.dto

import com.example.hhplus_ecommerce.order.OrderItem
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class OrderItemDtoTest {
	@Test
	@DisplayName("주문 아이템 도메인 모델로 OrderItemDto 생성하는 정적 팩토리 메서드 기능 테스트")
	fun orderItemDtoFromOrderItem() {
		val orderItem = OrderItem(
			orderId = 1L,
			productDetailId = 1L,
			quantity = 100,
			price = 5000
		)

		val actual = OrderItemDto.from(orderItem)

		assertThat(actual).isNotNull
		assertThat(actual.orderId).isEqualTo(1L)
		assertThat(actual.productDetailId).isEqualTo(1L)
		assertThat(actual.quantity).isEqualTo(100)
		assertThat(actual.price).isEqualTo(5000)
	}

}