package com.example.hhplus_ecommerce.api.cart

import com.example.hhplus_ecommerce.api.cart.request.CartAddRequest
import com.example.hhplus_ecommerce.api.cart.response.CartCommandResponse
import com.example.hhplus_ecommerce.api.cart.response.CartQueryResponse
import com.example.hhplus_ecommerce.api.error.ErrorBody
import com.example.hhplus_ecommerce.domain.cart.dto.CartItem
import com.example.hhplus_ecommerce.exception.BadRequestException
import com.example.hhplus_ecommerce.exception.NotFoundException
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Api(tags = ["장바구니 API"])
@RequestMapping("/api/v1")
@RestController
class CartApi() {
	/**
	 * 장바구니 상품 추가 API
	 */
	@PostMapping("/carts")
	@ApiOperation(value = "장바구니 상품 추가", notes = "상품을 장바구니에 추가한다.")
	@ApiResponses(value = [
		ApiResponse(code = 200, message = "OK", response = CartCommandResponse::class),
		ApiResponse(code = 400, message = "재고 부족", response = ErrorBody::class),
	])
	fun addCartItem(@RequestBody cartAddRequest: CartAddRequest): ResponseEntity<Any> {
		try {
			return ResponseEntity.ok(
				CartCommandResponse(
					"상품을 장바구니에 추가했습니다.",
					12345L,
					CartItem(1L, 2
					)
				)
			)
		} catch (e: BadRequestException) {
			return ResponseEntity(ErrorBody(e.errorStatus.message, 400), HttpStatus.BAD_REQUEST)
		}
	}

	/**
	 * 장바구니 상품 삭제 API
	 */
	@DeleteMapping("/carts/users/{userId}/products/{productId}")
	@ApiOperation(value = "장바구니 상품 삭제", notes = "장바구니에서 상품을 삭제한다.")
	@ApiResponses(value = [
		ApiResponse(code = 200, message = "OK", response = CartCommandResponse::class),
		ApiResponse(code = 404, message = "사용자 정보 없음", response = ErrorBody::class),
		ApiResponse(code = 404, message = "상품 없음", response = ErrorBody::class),
	])
	fun deleteCartItem(
		@PathVariable userId: Long,
		@PathVariable productId: Long
	): ResponseEntity<Any> {
		try {
			return ResponseEntity.ok(
				CartCommandResponse(
					"장바구니에서 상품을 삭제했습니다.",
					12345L,
					CartItem(2L, 1)
				)
			)
		} catch (e: NotFoundException) {
			return ResponseEntity(ErrorBody(e.errorStatus.message, 404), HttpStatus.NOT_FOUND)
		}
	}

	/**
	 * 장바구니 조회 API
	 */
	@GetMapping("/carts/users/{userId}")
	@ApiOperation(value = "장바구니 조회", notes = "장바구니에 담은 상품 목록을 조회한다.")
	@ApiResponses(value = [
		ApiResponse(code = 200, message = "OK", response = CartQueryResponse::class),
		ApiResponse(code = 404, message = "장바구니 없음", response = ErrorBody::class),
	])
	fun getCartsByUser(@PathVariable userId: Long): ResponseEntity<Any> {
		try {
			return ResponseEntity.ok(
				CartQueryResponse(
					12345L,
					listOf(
						CartItem(1L, 2),
						CartItem(2L, 1)
					)
				)
			)
		} catch (e: NotFoundException) {
			return ResponseEntity(ErrorBody(e.errorStatus.message, 404), HttpStatus.NOT_FOUND)
		}
	}
}