package com.example.hhplus_ecommerce.web.cart

import com.example.hhplus_ecommerce.web.cart.request.CartAddRequest
import com.example.hhplus_ecommerce.web.cart.response.CartCommandResponse
import com.example.hhplus_ecommerce.web.cart.response.CartQueryResponse
import com.example.hhplus_ecommerce.domain.share.exception.ExceptionBody
import com.example.hhplus_ecommerce.domain.cart.CartService
import com.example.hhplus_ecommerce.domain.cart.dto.CartDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "장바구니 API")
@RequestMapping("/api/v1")
@RestController
class CartApi(private val cartService: CartService) {
	/**
	 * 장바구니 상품 추가 API
	 */
	@PostMapping("/carts")
	@Operation(summary = "장바구니 상품 추가", description = "상품을 장바구니에 추가한다.")
	@ApiResponses(value = [
		ApiResponse(responseCode = "200", description = "장바구니 상품 추가 성공",
			content = [ Content(mediaType = "application/json", schema = Schema(implementation = CartCommandResponse::class)) ]),
	])
	fun addCartItem(@RequestBody cartAddRequest: CartAddRequest): ResponseEntity<CartCommandResponse> {
		return ResponseEntity.ok(
			CartCommandResponse.of(
				"상품을 장바구니에 추가했습니다.",
				cartService.addProductCart(CartDto.from(cartAddRequest))
			)
		)
	}

	/**
	 * 장바구니 상품 삭제 API
	 */
	@DeleteMapping("/carts/users/{userId}/products/{productId}")
	@Operation(summary = "장바구니 상품 삭제", description = "장바구니에서 상품을 삭제한다.")
	@ApiResponses(value = [
		ApiResponse(responseCode = "200", description = "장바구니 상품 삭제 성공",
			content = [ Content(mediaType = "application/json", schema = Schema(implementation = CartCommandResponse::class)) ]),
		ApiResponse(responseCode = "404", description = "장바구니 없음",
			content = [ Content(mediaType = "application/json", schema = Schema(implementation = ExceptionBody::class)) ]),
	])
	fun deleteCartItem(
		@PathVariable userId: Long,
		@PathVariable productId: Long
	): ResponseEntity<CartCommandResponse> {
		return ResponseEntity.ok(
			CartCommandResponse.of(
				"장바구니에서 상품을 삭제했습니다.",
				cartService.deleteCartByUserProduct(userId, productId)
			)
		)
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
			content = [ Content(mediaType = "application/json", schema = Schema(implementation = ExceptionBody::class)) ]),
	])
	fun getCartsByUser(@PathVariable userId: Long): ResponseEntity<CartQueryResponse> {
		return ResponseEntity.ok(
			CartQueryResponse.of(
				userId,
				cartService.getAllCartsByUser(userId)
			)
		)
	}
}