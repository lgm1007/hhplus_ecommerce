package com.example.hhplus_ecommerce.api.cart

import com.example.hhplus_ecommerce.api.cart.request.CartAddRequest
import com.example.hhplus_ecommerce.api.cart.response.CartCommandResponse
import com.example.hhplus_ecommerce.api.cart.response.CartQueryResponse
import com.example.hhplus_ecommerce.api.error.ErrorBody
import com.example.hhplus_ecommerce.domain.cart.dto.CartItem
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

@Tag(name = "장바구니 API")
@RequestMapping("/api/v1")
@RestController
class CartApi() {
	/**
	 * 장바구니 상품 추가 API
	 */
	@PostMapping("/carts")
	@Operation(summary = "장바구니 상품 추가", description = "상품을 장바구니에 추가한다.")
	@ApiResponses(value = [
		ApiResponse(responseCode = "200", description = "장바구니 상품 추가 성공",
			content = [ Content(mediaType = "application/json", schema = Schema(implementation = CartCommandResponse::class)) ]),
		ApiResponse(responseCode = "400", description = "재고 부족",
			content = [ Content(mediaType = "application/json", schema = Schema(implementation = ErrorBody::class)) ]),
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
	@Operation(summary = "장바구니 상품 삭제", description = "장바구니에서 상품을 삭제한다.")
	@ApiResponses(value = [
		ApiResponse(responseCode = "200", description = "장바구니 상품 삭제 성공",
			content = [ Content(mediaType = "application/json", schema = Schema(implementation = CartCommandResponse::class)) ]),
		ApiResponse(responseCode = "404", description = "사용자 정보/상품 없음",
			content = [ Content(mediaType = "application/json", schema = Schema(implementation = ErrorBody::class)) ]),
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
	@Operation(summary = "장바구니 조회", description = "장바구니에 담은 상품 목록을 조회한다.")
	@ApiResponses(value = [
		ApiResponse(responseCode = "200", description = "장바구니 조회 성공",
			content = [ Content(mediaType = "application/json", schema = Schema(implementation = CartQueryResponse::class)) ]),
		ApiResponse(responseCode = "404", description = "장바구니 없음",
			content = [ Content(mediaType = "application/json", schema = Schema(implementation = ErrorBody::class)) ]),
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