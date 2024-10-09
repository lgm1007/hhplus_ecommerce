package com.example.hhplus_ecommerce.api.payment

import com.example.hhplus_ecommerce.api.error.ErrorBody
import com.example.hhplus_ecommerce.api.error.ErrorStatus
import com.example.hhplus_ecommerce.api.payment.request.PaymentRequest
import com.example.hhplus_ecommerce.api.payment.response.PaymentResponse
import com.example.hhplus_ecommerce.exception.BadRequestException
import com.example.hhplus_ecommerce.exception.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RequestMapping("/api/v1")
@RestController
class PaymentApi() {
	/**
	 * 결제 API
	 */
	@PostMapping("/payments")
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
		} catch (badException: BadRequestException) {
			return ResponseEntity(ErrorBody(ErrorStatus.NOT_ENOUGH_BALANCE.message, 400), HttpStatus.BAD_REQUEST)
		} catch (notFoundException: NotFoundException) {
			return ResponseEntity(ErrorBody(ErrorStatus.NOT_FOUND_ORDER.message, 404), HttpStatus.NOT_FOUND)
		}
	}
}