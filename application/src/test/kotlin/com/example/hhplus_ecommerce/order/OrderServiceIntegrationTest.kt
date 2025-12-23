package com.example.hhplus_ecommerce.order

import com.example.hhplus_ecommerce.order.dto.OrderDto
import com.example.hhplus_ecommerce.order.dto.OrderItemDto
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

@SpringBootTest
class OrderServiceIntegrationTest {
	@Autowired private lateinit var orderService: OrderService
	@Autowired private lateinit var orderRepository: OrderJpaRepository
	@Autowired private lateinit var orderItemRepository: OrderItemJpaRepository

	@BeforeEach
	fun clearDB() {
		orderRepository.deleteAll()
		orderItemRepository.deleteAll()
	}

	@Test
	@DisplayName("주문 정보 저장하기 기능 테스트")
	fun registerOrder() {
		val orderDto = OrderDto(
			orderId = 0,
			userId = 1L,
			orderDate = LocalDateTime.now(),
			totalPrice = 10000,
			orderStatus = OrderStatus.ORDER_COMPLETE,
			createdDate = LocalDateTime.now(),
			lastModifiedDate = LocalDateTime.now()
		)

		val orderId = orderService.insertOrder(orderDto).orderId!!

		val actual = orderService.getOrderById(orderId)

		assertThat(actual.totalPrice).isEqualTo(10000)
		assertThat(actual.orderStatus).isEqualTo(OrderStatus.ORDER_COMPLETE)
	}

	@Test
	@DisplayName("주문 상품 목록 저장하기 기능 테스트")
	fun registerOrderItems() {
		val orderItemDtos = listOf(
			OrderItemDto(0, 1L, 1L, 1, 1000, LocalDateTime.now()),
			OrderItemDto(0, 1L, 2L, 2, 5000, LocalDateTime.now()),
		)

		orderService.insertAllOrderItems(orderItemDtos)

		val actual = orderItemRepository.findAllByOrderId(1L)

		assertThat(actual.size).isEqualTo(2)
	}

	@Test
	@DisplayName("주문 상태 업데이트 기능 테스트")
	fun updateOrderStatus() {
		val orderDto = OrderDto(
			orderId = 0,
			userId = 1L,
			orderDate = LocalDateTime.now(),
			totalPrice = 10000,
			orderStatus = OrderStatus.ORDER_WAIT,
			createdDate = LocalDateTime.now(),
			lastModifiedDate = LocalDateTime.now()
		)
		val orderId = orderService.insertOrder(orderDto).orderId!!

		orderService.updateOrderStatus(orderId, OrderStatus.ORDER_COMPLETE)

		val actual = orderService.getOrderById(orderId)

		assertThat(actual.orderStatus).isEqualTo(OrderStatus.ORDER_COMPLETE)
	}
}
