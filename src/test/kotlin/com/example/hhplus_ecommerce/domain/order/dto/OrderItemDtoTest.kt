package com.example.hhplus_ecommerce.domain.order.dto

import com.example.hhplus_ecommerce.infrastructure.order.entity.OrderItem
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class OrderItemDtoTest {
	@Test
	@DisplayName("주문 아이템 Entity로 OrderItemDto 생성하는 정적 팩토리 메서드 기능 테스트")
	fun orderItemDtoFromOrderItem() {
		val orderItem = OrderItem(1L, 1L, 100, 5000)

		val actual = OrderItemDto.from(orderItem)

		assertThat(actual).isNotNull
		assertThat(actual.id).isEqualTo(0)
		assertThat(actual.orderId).isEqualTo(1L)
		assertThat(actual.productDetailId).isEqualTo(1L)
		assertThat(actual.quantity).isEqualTo(100)
		assertThat(actual.price).isEqualTo(5000)
	}

}