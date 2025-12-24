package com.example.hhplus_ecommerce.cart.request

import com.example.hhplus_ecommerce.cart.dto.CartDto

data class CartAddRequest(
	val userId: Long,
	val productDetailId: Long,
	val quantity: Int
) {
	fun toDto(): CartDto {
		return CartDto(
			userId = userId,
			productDetailId = productDetailId,
			quantity = quantity
		)
	}
}