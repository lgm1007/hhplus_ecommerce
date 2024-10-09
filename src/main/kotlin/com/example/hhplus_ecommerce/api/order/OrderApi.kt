package com.example.hhplus_ecommerce.api.order

import com.example.hhplus_ecommerce.api.error.ErrorBody
import com.example.hhplus_ecommerce.api.error.ErrorStatus
import com.example.hhplus_ecommerce.api.order.request.OrderRequest
import com.example.hhplus_ecommerce.api.order.response.OrderResponse
import com.example.hhplus_ecommerce.domain.order.OrderStatus
import com.example.hhplus_ecommerce.domain.order.dto.OrderItemDetail
import com.example.hhplus_ecommerce.exception.BadRequestException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RequestMapping("/api/v1")
@RestController
class OrderApi() {
	/**
	 * 주문 API
	 */
	@PostMapping("/orders")
	fun order(@RequestBody orderRequest: OrderRequest): ResponseEntity<Any> {
		try {
			return ResponseEntity.ok(
				OrderResponse(
					98765L,
					12345L,
					LocalDateTime.of(2024, 10, 6, 12, 0, 1),
					13000,
					OrderStatus.ORDER_COMPLETE,
					listOf(
						OrderItemDetail(1L, 2, 10000),
						OrderItemDetail(2L, 1, 3000)
					)
				)
			)
		} catch (e: BadRequestException) {
			return ResponseEntity(ErrorBody(ErrorStatus.NOT_ENOUGH_BALANCE.message, 400), HttpStatus.BAD_REQUEST)
		}
	}
}