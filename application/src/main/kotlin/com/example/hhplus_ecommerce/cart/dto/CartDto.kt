package com.example.hhplus_ecommerce.cart.dto

import com.example.hhplus_ecommerce.cart.Cart
import java.time.LocalDateTime

class CartDto(
	val cartId: Long? = null,
	val userId: Long,
	val productDetailId: Long,
	val quantity: Int,
	val createdDate: LocalDateTime? = null
) {
	companion object {
		fun from(cart: Cart): CartDto {
			return CartDto(
				cartId = cart.cartId,
				userId = cart.userId,
				productDetailId = cart.productDetailId,
				quantity = cart.quantity,
				createdDate = cart.createdDate
			)
		}

		fun fromList(carts: List<Cart>): List<CartDto> {
			return carts.map(::from)
		}
	}
}