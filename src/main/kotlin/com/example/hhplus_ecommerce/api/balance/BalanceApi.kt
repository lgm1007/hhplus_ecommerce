package com.example.hhplus_ecommerce.api.balance

import com.example.hhplus_ecommerce.api.balance.request.BalanceChargeRequest
import com.example.hhplus_ecommerce.api.balance.response.BalanceChargeResponse
import com.example.hhplus_ecommerce.api.balance.response.UserBalanceResponse
import com.example.hhplus_ecommerce.api.error.ErrorBody
import com.example.hhplus_ecommerce.domain.balance.BalanceService
import com.example.hhplus_ecommerce.exception.BadRequestException
import com.example.hhplus_ecommerce.exception.NotFoundException
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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
			content = [ Content(mediaType = "application/json", schema = Schema(implementation = ErrorBody::class)) ]),
	])
	fun chargeBalance(@RequestBody chargeRequest: BalanceChargeRequest): ResponseEntity<Any> {
		try {
			return ResponseEntity.ok(
				BalanceChargeResponse.of(
					balanceService.updateAmountCharge(chargeRequest.userId, chargeRequest.amount),
					chargeRequest.amount
				)
			)
		} catch (e: BadRequestException) {
			return ResponseEntity(ErrorBody(e.errorStatus.message, 400), HttpStatus.BAD_REQUEST)
		}
	}

	/**
	 * 잔액 조회 API
	 */
	@GetMapping("/balances/users/{userId}")
	@Operation(summary = "잔액 조회", description = "사용자의 현재 잔액을 조회한다.")
	@ApiResponses(value = [
		ApiResponse(responseCode = "200", description = "잔액 조회 성공",
			content = [ Content(mediaType = "application/json", schema = Schema(implementation = UserBalanceResponse::class)) ]),
		ApiResponse(responseCode = "404", description = "사용자 없음",
			content = [ Content(mediaType = "application/json", schema = Schema(implementation = ErrorBody::class)) ]),
	])
	fun fetchUserCurrentBalance(@PathVariable userId: Long): ResponseEntity<Any> {
		try {
			return ResponseEntity.ok(
				UserBalanceResponse.from(balanceService.getByUserIdWithLock(userId))
			)
		} catch (e: NotFoundException) {
			return ResponseEntity(ErrorBody(e.errorStatus.message, 404), HttpStatus.NOT_FOUND)
		}
	}

}