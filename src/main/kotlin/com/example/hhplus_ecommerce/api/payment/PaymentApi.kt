package com.example.hhplus_ecommerce.api.payment

import com.example.hhplus_ecommerce.api.error.ErrorBody
import com.example.hhplus_ecommerce.api.payment.request.PaymentRequest
import com.example.hhplus_ecommerce.api.payment.response.PaymentResponse
import com.example.hhplus_ecommerce.exception.BadRequestException
import com.example.hhplus_ecommerce.exception.NotFoundException
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

@Api(tags = ["결제 API"])
@RequestMapping("/api/v1")
@RestController
class PaymentApi() {
	/**
	 * 결제 API
	 */
	@PostMapping("/payments")
	@ApiOperation(value = "주문 결제", notes = "주문에 대해 결제한다.")
	@ApiResponses(value = [
		ApiResponse(code = 200, message = "OK", response = PaymentResponse::class),
		ApiResponse(code = 400, message = "잔액 부족", response = ErrorBody::class),
		ApiResponse(code = 404, message = "주문 정보 없음", response = ErrorBody::class),
	])
	fun payment(@RequestBody paymentRequest: PaymentRequest): ResponseEntity<Any> {
		try {
			return ResponseEntity.ok(
				PaymentResponse(
					123L,
					98765L,
					17000,
					LocalDateTime.of(2024, 10, 6, 12, 1, 10)
				)
			)
		} catch (badRequestException: BadRequestException) {
			return ResponseEntity(ErrorBody(badRequestException.errorStatus.message, 400), HttpStatus.BAD_REQUEST)
		} catch (notFoundException: NotFoundException) {
			return ResponseEntity(ErrorBody(notFoundException.errorStatus.message, 404), HttpStatus.NOT_FOUND)
		}
	}
}