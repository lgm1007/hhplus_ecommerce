package com.example.hhplus_ecommerce.api.order

import com.example.hhplus_ecommerce.api.error.ErrorBody
import com.example.hhplus_ecommerce.api.order.request.OrderRequest
import com.example.hhplus_ecommerce.api.order.response.OrderResponse
import com.example.hhplus_ecommerce.domain.order.OrderStatus
import com.example.hhplus_ecommerce.domain.order.dto.OrderItemDetailInfo
import com.example.hhplus_ecommerce.exception.BadRequestException
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@Tag(name = "주문 API")
@RequestMapping("/api/v1")
@RestController
class OrderApi() {
	/**
	 * 주문 API
	 */
	@PostMapping("/orders")
	@Operation(summary = "상품 주문", description = "상품을 주문한다.")
	@ApiResponses(value = [
		ApiResponse(responseCode = "200", description = "상품 주문 성공",
			content = [ Content(mediaType = "application/json", schema = Schema(implementation = OrderResponse::class)) ]),
		ApiResponse(responseCode = "400", description = "잔액/재고 부족",
			content = [ Content(mediaType = "application/json", schema = Schema(implementation = ErrorBody::class)) ]),
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
						OrderItemDetailInfo(1L, 2, 10000),
						OrderItemDetailInfo(2L, 1, 3000)
					)
				)
			)
		} catch (e: BadRequestException) {
			return ResponseEntity(ErrorBody(e.errorStatus.message, 400), HttpStatus.BAD_REQUEST)
		}
	}
}