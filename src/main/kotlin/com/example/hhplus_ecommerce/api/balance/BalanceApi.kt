package com.example.hhplus_ecommerce.api.balance

import com.example.hhplus_ecommerce.api.balance.request.BalanceChargeRequest
import com.example.hhplus_ecommerce.api.balance.response.BalanceChargeResponse
import com.example.hhplus_ecommerce.api.balance.response.UserBalanceResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/v1")
@RestController
class BalanceApi() {
	/**
	 * 잔액 충전 API
	 */
	@PostMapping("/balance/charge")
	fun chargeBalance(@RequestBody chargeRequest: BalanceChargeRequest): ResponseEntity<BalanceChargeResponse> {
		return ResponseEntity.ok(
			BalanceChargeResponse(12345L, 10000, 30000)
		)
	}

	/**
	 * 잔액 조회 API
	 */
	@GetMapping("/balance/user/{userId}")
	fun fetchUserCurrentBalance(@PathVariable userId: Long): ResponseEntity<UserBalanceResponse> {
		return ResponseEntity.ok(
			UserBalanceResponse(12345L, 30000)
		)
	}

}