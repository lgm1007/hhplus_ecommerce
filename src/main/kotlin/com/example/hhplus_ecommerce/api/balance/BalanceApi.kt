package com.example.hhplus_ecommerce.api.balance

import com.example.hhplus_ecommerce.api.balance.request.BalanceChargeRequest
import com.example.hhplus_ecommerce.api.balance.response.BalanceChargeResponse
import com.example.hhplus_ecommerce.api.balance.response.UserBalanceResponse
import com.example.hhplus_ecommerce.api.error.ErrorBody
import com.example.hhplus_ecommerce.api.error.ErrorStatus
import com.example.hhplus_ecommerce.exception.BadRequestException
import com.example.hhplus_ecommerce.exception.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/v1")
@RestController
class BalanceApi() {
	/**
	 * 잔액 충전 API
	 */
	@PostMapping("/balances")
	fun chargeBalance(@RequestBody chargeRequest: BalanceChargeRequest): ResponseEntity<Any> {
		try {
			return ResponseEntity.ok(
				BalanceChargeResponse(12345L, 10000, 30000)
			)
		} catch (e: BadRequestException) {
			return ResponseEntity(ErrorBody(ErrorStatus.CHARGED_AMOUNT_ERROR.message, 400), HttpStatus.BAD_REQUEST)
		}
	}

	/**
	 * 잔액 조회 API
	 */
	@GetMapping("/balances/users/{userId}")
	fun fetchUserCurrentBalance(@PathVariable userId: Long): ResponseEntity<Any> {
		try {
			return ResponseEntity.ok(
				UserBalanceResponse(12345L, 30000)
			)
		} catch (e: NotFoundException) {
			return ResponseEntity(ErrorBody(ErrorStatus.NOT_FOUND_USER.message, 404), HttpStatus.NOT_FOUND)
		}
	}

}