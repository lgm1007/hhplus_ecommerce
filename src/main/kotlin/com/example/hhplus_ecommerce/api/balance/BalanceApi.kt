package com.example.hhplus_ecommerce.api.balance

import com.example.hhplus_ecommerce.api.balance.request.BalanceChargeRequest
import com.example.hhplus_ecommerce.api.balance.response.BalanceChargeResponse
import com.example.hhplus_ecommerce.api.balance.response.UserBalanceResponse
import com.example.hhplus_ecommerce.api.error.ErrorBody
import com.example.hhplus_ecommerce.exception.BadRequestException
import com.example.hhplus_ecommerce.exception.NotFoundException
import io.swagger.annotations.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Api(tags = ["잔액 API"])
@RequestMapping("/api/v1")
@RestController
class BalanceApi() {
	@PostMapping("/balances")
	@ApiOperation(value = "잔액 충전", notes = "사용자의 잔액을 충전한다.")
	@ApiResponses(value = [
		ApiResponse(code = 200, message = "OK", response = BalanceChargeResponse::class),
		ApiResponse(code = 400, message = "충전 금액 에러", response = ErrorBody::class),
	])
	fun chargeBalance(@RequestBody chargeRequest: BalanceChargeRequest): ResponseEntity<Any> {
		try {
			return ResponseEntity.ok(
				BalanceChargeResponse(12345L, 10000, 30000)
			)
		} catch (e: BadRequestException) {
			return ResponseEntity(ErrorBody(e.errorStatus.message, 400), HttpStatus.BAD_REQUEST)
		}
	}

	/**
	 * 잔액 조회 API
	 */
	@GetMapping("/balances/users/{userId}")
	@ApiOperation(value = "잔액 조회", notes = "사용자의 현재 잔액을 조회한다.")
	@ApiResponses(value = [
		ApiResponse(code = 200, message = "OK", response = UserBalanceResponse::class),
		ApiResponse(code = 404, message = "사용자 없음", response = ErrorBody::class),
	])
	fun fetchUserCurrentBalance(@PathVariable userId: Long): ResponseEntity<Any> {
		try {
			return ResponseEntity.ok(
				UserBalanceResponse(12345L, 30000)
			)
		} catch (e: NotFoundException) {
			return ResponseEntity(ErrorBody(e.errorStatus.message, 404), HttpStatus.NOT_FOUND)
		}
	}

}