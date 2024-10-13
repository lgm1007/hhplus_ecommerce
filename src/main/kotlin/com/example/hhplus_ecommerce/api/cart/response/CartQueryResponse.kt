package com.example.hhplus_ecommerce.api.cart.response

import com.example.hhplus_ecommerce.domain.cart.dto.CartItem

class CartQueryResponse(
	val userId: Long,
	val carts: List<CartItem>
) {
}