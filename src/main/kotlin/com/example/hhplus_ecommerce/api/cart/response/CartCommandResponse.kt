package com.example.hhplus_ecommerce.api.cart.response

import com.example.hhplus_ecommerce.domain.cart.dto.CartItem

class CartCommandResponse(
	val message: String,
	val userId: Long,
	val cart: CartItem
) {
}