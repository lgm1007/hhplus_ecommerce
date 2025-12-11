package com.example.hhplus_ecommerce.web.cart.response

import com.example.hhplus_ecommerce.domain.cart.dto.CartDto
import com.example.hhplus_ecommerce.domain.cart.dto.CartResponseItem

class CartCommandResponse(
	val message: String,
	val userId: Long,
	val cart: CartResponseItem
) {
	companion object {
		fun of(message: String, cartDto: CartDto): CartCommandResponse {
			return CartCommandResponse(
				message,
				cartDto.userId,
				CartResponseItem.from(cartDto)
			)
		}
	}
}