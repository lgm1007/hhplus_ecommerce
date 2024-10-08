package com.example.hhplus_ecommerce.api.order

import com.example.hhplus_ecommerce.api.order.request.OrderRequest
import com.example.hhplus_ecommerce.domain.order.dto.OrderItemDetail
import com.example.hhplus_ecommerce.api.order.response.OrderResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RequestMapping("/api/v1")
@RestController
class OrderApi() {
	/**
	 * 주문 API
	 */
	@PostMapping("/orders")
	fun order(@RequestBody orderRequest: OrderRequest): ResponseEntity<OrderResponse> {
		return ResponseEntity.ok(
			OrderResponse(
				98765L,
				12345L,
				LocalDate.of(2024, 10, 6),
				13000,
				listOf(
					OrderItemDetail(1L, 2, 10000),
					OrderItemDetail(2L, 1, 3000)
				)
			)
		)
	}
}