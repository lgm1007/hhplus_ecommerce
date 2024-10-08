package com.example.hhplus_ecommerce.api.cart.response

import com.example.hhplus_ecommerce.domain.cart.dto.CurrentCart

class CartResponse(
	val message: String,
	val cart: CurrentCart
) {
}