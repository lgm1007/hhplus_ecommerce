package com.example.hhplus_ecommerce.domain.cart.dto

class CurrentCart(
	val userId: Long,
	val items: List<CartItem>
) {
}