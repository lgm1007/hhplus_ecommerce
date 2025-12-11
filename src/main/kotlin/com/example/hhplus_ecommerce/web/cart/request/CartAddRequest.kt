package com.example.hhplus_ecommerce.web.cart.request

class CartAddRequest(
	val userId: Long,
	val productId: Long,
	val quantity: Int
) {
}