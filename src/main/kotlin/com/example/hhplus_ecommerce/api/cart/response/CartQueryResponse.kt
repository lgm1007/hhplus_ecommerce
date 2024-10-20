package com.example.hhplus_ecommerce.api.cart.response

import com.example.hhplus_ecommerce.domain.cart.dto.CartDto
import com.example.hhplus_ecommerce.domain.cart.dto.CartResponseItem

class CartQueryResponse(
	val userId: Long,
	val carts: List<CartResponseItem>
) {
	companion object {
		fun of(userId: Long, cartDtos: List<CartDto>): CartQueryResponse {
			return CartQueryResponse(
				userId,
				CartResponseItem.fromList(cartDtos)
			)
		}
	}
}