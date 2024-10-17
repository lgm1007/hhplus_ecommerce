package com.example.hhplus_ecommerce.domain.order.dto

import org.assertj.core.api.AssertionsForClassTypes
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class OrderItemDetailInfoTest {
	@Test
	@DisplayName("주문한 상품의 총 가격 (가격 * 주문 개수) 을 계산하는 기능 테스트")
	fun calculateOrderPrice() {
		val orderItemDetailInfo = OrderItemDetailInfo(1L, 2, 10000)

		assertThat(orderItemDetailInfo.calculateOrderPrice()).isEqualTo(20000)
	}
}