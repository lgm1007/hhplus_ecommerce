package com.example.hhplus_ecommerce.cart.dto

class CartResponseItem(
	val productDetailId: Long,
	val quantity: Int
) {
	companion object {
		fun from(cartDto: CartDto): CartResponseItem {
			return CartResponseItem(cartDto.productDetailId, cartDto.quantity)
		}

		fun fromList(cartDtos: List<CartDto>): List<CartResponseItem> {
			return cartDtos.map(::from)
		}
	}
}