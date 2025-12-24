package com.example.hhplus_ecommerce.cart.response

import com.example.hhplus_ecommerce.cart.dto.CartDto
import com.example.hhplus_ecommerce.cart.dto.CartResponseItem

data class CartCommandResponse(
	val message: String,
	val userId: Long,
	val cartResponse: CartResponseItem
) {
	companion object {
		fun of(message: String, cartDto: CartDto): CartCommandResponse {
			return CartCommandResponse(
				message = message,
				userId = cartDto.userId,
				cartResponse = CartResponseItem.from(cartDto)
			)
		}
	}
}