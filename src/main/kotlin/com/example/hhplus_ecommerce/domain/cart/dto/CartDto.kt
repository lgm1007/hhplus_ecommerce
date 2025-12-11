package com.example.hhplus_ecommerce.domain.cart.dto

import com.example.hhplus_ecommerce.web.cart.request.CartAddRequest
import com.example.hhplus_ecommerce.infrastructure.cart.entity.Cart
import java.time.LocalDateTime

class CartDto(
	val id: Long,
	val userId: Long,
	val productDetailId: Long,
	val quantity: Int,
	val createdDate: LocalDateTime
) {
	companion object {
		fun from(cart: Cart): CartDto {
			return CartDto(
				cart.id,
				cart.userId,
				cart.productDetailId,
				cart.quantity,
				cart.createdDate
			)
		}

		fun fromList(carts: List<Cart>): List<CartDto> {
			return carts.map(::from)
		}

		fun from(cartAddRequest: CartAddRequest): CartDto {
			return CartDto(
				0,
				cartAddRequest.userId,
				cartAddRequest.productId,
				cartAddRequest.quantity,
				LocalDateTime.now()
			)
		}
	}
}