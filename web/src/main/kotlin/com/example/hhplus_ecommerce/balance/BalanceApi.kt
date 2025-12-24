package com.example.hhplus_ecommerce.balance

import com.example.hhplus_ecommerce.balance.request.BalanceChargeRequest
import com.example.hhplus_ecommerce.balance.response.BalanceChargeResponse
import com.example.hhplus_ecommerce.balance.response.UserBalanceResponse
import com.example.hhplus_ecommerce.share.exception.ExceptionBody
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "잔액 API")
@RequestMapping("/api/v1")
@RestController
class BalanceApi(private val balanceService: BalanceService) {
	@PostMapping("/balances")
	@Operation(summary = "잔액 충전", description = "사용자의 잔액을 충전한다.")
	@ApiResponses(value = [
		ApiResponse(responseCode = "200", description = "잔액 충전 성공",
			content = [ Content(mediaType = "application/json", schema = Schema(implementation = BalanceChargeResponse::class)) ]),
		ApiResponse(responseCode = "400", description = "충전 금액 에러",
			content = [ Content(mediaType = "application/json", schema = Schema(implementation = ExceptionBody::class)) ]),
		ApiResponse(responseCode = "404", description = "사용자 잔액 정보 없음",
			content = [ Content(mediaType = "application/json", schema = Schema(implementation = ExceptionBody::class)) ]),
	])
	fun chargeBalance(@RequestBody chargeRequest: BalanceChargeRequest): ResponseEntity<BalanceChargeResponse> {
		return ResponseEntity.ok(
			BalanceChargeResponse.of(
				balanceDto = balanceService.updateAmountCharge(userId = chargeRequest.userId, amount = chargeRequest.amount),
				chargedAmount = chargeRequest.amount
			)
		)
	}

	/**
	 * 잔액 조회 API
	 */
	@GetMapping("/balances/users/{userId}")
	@Operation(summary = "잔액 조회", description = "사용자의 현재 잔액을 조회한다.")
	@ApiResponses(value = [
		ApiResponse(responseCode = "200", description = "잔액 조회 성공",
			content = [ Content(mediaType = "application/json", schema = Schema(implementation = UserBalanceResponse::class)) ]),
		ApiResponse(responseCode = "404", description = "사용자 잔액 정보 없음",
			content = [ Content(mediaType = "application/json", schema = Schema(implementation = ExceptionBody::class)) ]),
	])
	fun fetchUserCurrentBalance(@PathVariable userId: Long): ResponseEntity<UserBalanceResponse> {
		return ResponseEntity.ok(
			UserBalanceResponse.from(
				balanceDto = balanceService.getByUserId(userId)
			)
		)
	}

}