package com.example.hhplus_ecommerce.web.payment

import com.example.hhplus_ecommerce.domain.share.exception.ExceptionBody
import com.example.hhplus_ecommerce.web.payment.request.PaymentRequest
import com.example.hhplus_ecommerce.web.payment.response.PaymentResponse
import com.example.hhplus_ecommerce.application.payment.PaymentFacade
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

@Tag(name = "결제 API")
@RequestMapping("/api/v1")
@RestController
class PaymentApi(private val paymentFacade: PaymentFacade) {
	/**
	 * 결제 API
	 */
	@PostMapping("/payments")
	@Operation(summary = "주문 결제", description = "주문에 대해 결제한다.")
	@ApiResponses(value = [
		ApiResponse(responseCode = "200", description = "주문 결제 성공",
			content = [ Content(mediaType = "application/json", schema = Schema(implementation = PaymentResponse::class)) ]),
		ApiResponse(responseCode = "400", description = "잔액 부족",
			content = [ Content(mediaType = "application/json", schema = Schema(implementation = ExceptionBody::class)) ]),
		ApiResponse(responseCode = "404", description = "주문 정보 없음",
			content = [ Content(mediaType = "application/json", schema = Schema(implementation = ExceptionBody::class)) ]),
	])
	fun payment(@RequestBody paymentRequest: PaymentRequest): ResponseEntity<PaymentResponse> {
		return ResponseEntity.ok(
			PaymentResponse.from(paymentFacade.orderPayment(paymentRequest.userId, paymentRequest.orderId))
		)
	}
}