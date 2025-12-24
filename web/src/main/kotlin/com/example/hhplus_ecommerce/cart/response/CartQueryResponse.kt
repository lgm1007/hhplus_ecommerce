package com.example.hhplus_ecommerce.cart.response

import com.example.hhplus_ecommerce.cart.dto.CartDto
import com.example.hhplus_ecommerce.cart.dto.CartResponseItem

data class CartQueryResponse(
	val userId: Long,
	val cartResponses: List<CartResponseItem>
) {
	companion object {
		fun of(userId: Long, cartDtos: List<CartDto>): CartQueryResponse {
			return CartQueryResponse(
				userId = userId,
				cartResponses = CartResponseItem.fromList(cartDtos)
			)
		}
	}
}