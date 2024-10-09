package com.example.hhplus_ecommerce.api.order

import com.example.hhplus_ecommerce.api.error.ErrorBody
import com.example.hhplus_ecommerce.api.order.request.OrderRequest
import com.example.hhplus_ecommerce.api.order.response.OrderResponse
import com.example.hhplus_ecommerce.domain.order.OrderStatus
import com.example.hhplus_ecommerce.domain.order.dto.OrderItemDetail
import com.example.hhplus_ecommerce.exception.BadRequestException
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@Api(tags = ["주문 API"])
@RequestMapping("/api/v1")
@RestController
class OrderApi() {
	/**
	 * 주문 API
	 */
	@PostMapping("/orders")
	@ApiOperation(value = "상품 주문", notes = "상품을 주문한다.")
	@ApiResponses(value = [
		ApiResponse(code = 200, message = "OK", response = OrderResponse::class),
		ApiResponse(code = 400, message = "잔액 부족", response = ErrorBody::class),
		ApiResponse(code = 400, message = "재고 부족", response = ErrorBody::class),
	])
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
			return ResponseEntity(ErrorBody(e.errorStatus.message, 400), HttpStatus.BAD_REQUEST)
		}
	}
}