package com.example.hhplus_ecommerce.infrastructure.order.entity

import com.example.hhplus_ecommerce.domain.order.OrderStatus
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class OrderTest {
    @Test
    @DisplayName("주문 상태 업데이트 기능 테스트")
    fun updateOrderStatus() {
        val order = Order(
            1L,
            LocalDateTime.now(),
            5000,
            OrderStatus.ORDER_WAIT
        )

        order.updateStatus(OrderStatus.ORDER_COMPLETE)

        assertThat(order.orderStatus).isEqualTo(OrderStatus.ORDER_COMPLETE)
    }
}