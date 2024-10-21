package com.example.hhplus_ecommerce.api.order

import com.example.hhplus_ecommerce.api.error.ErrorBody
import com.example.hhplus_ecommerce.api.order.request.OrderRequest
import com.example.hhplus_ecommerce.api.order.response.OrderResponse
import com.example.hhplus_ecommerce.usecase.order.OrderFacade
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "주문 API")
@RequestMapping("/api/v1")
@RestController
class OrderApi(private val orderFacade: OrderFacade) {
	/**
	 * 주문 API
	 */
	@PostMapping("/orders")
	@Operation(summary = "상품 주문", description = "상품을 주문한다.")
	@ApiResponses(value = [
		ApiResponse(responseCode = "200", description = "상품 주문 성공",
			content = [ Content(mediaType = "application/json", schema = Schema(implementation = OrderResponse::class)) ]),
		ApiResponse(responseCode = "400", description = "재고 부족",
			content = [ Content(mediaType = "application/json", schema = Schema(implementation = ErrorBody::class)) ]),
		ApiResponse(responseCode = "404", description = "사용자의 비용 정보 없음",
			content = [ Content(mediaType = "application/json", schema = Schema(implementation = ErrorBody::class)) ]),
	])
	fun order(@RequestBody orderRequest: OrderRequest): ResponseEntity<OrderResponse> {
		return ResponseEntity.ok(
			OrderResponse.from(
				orderFacade.productOrder(orderRequest.userId, orderRequest.orderItemInfos)
			)
		)
	}
}