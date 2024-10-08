package com.example.hhplus_ecommerce.api.cart

import com.example.hhplus_ecommerce.api.cart.request.CartAddRequest
import com.example.hhplus_ecommerce.domain.cart.dto.CartItem
import com.example.hhplus_ecommerce.api.cart.response.CartResponse
import com.example.hhplus_ecommerce.domain.cart.dto.CurrentCart
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/v1")
@RestController
class CartApi() {
	/**
	 * 장바구니 상품 추가 API
	 */
	@PostMapping("/cart")
	fun addCartItem(@RequestBody cartAddRequest: CartAddRequest): ResponseEntity<CartResponse> {
		return ResponseEntity.ok(
			CartResponse(
				"상품을 장바구니에 추가했습니다.",
				CurrentCart(
					12345L,
					listOf(
						CartItem(1L, 2)
					)
				)
			)
		)
	}

	/**
	 * 장바구니 상품 삭제 API
	 */
	@DeleteMapping("/cart/user/{userId}/product/{productId}")
	fun deleteCartItem(
		@PathVariable userId: Long,
		@PathVariable productId: Long
	): ResponseEntity<CartResponse> {
		return ResponseEntity.ok(
			CartResponse(
				"장바구니에서 상품을 삭제했습니다.",
				CurrentCart(
					12345L,
					listOf(
						CartItem(2L, 1)
					)
				)
			)
		)
	}
}