package com.example.hhplus_ecommerce.domain.cart

import com.example.hhplus_ecommerce.domain.cart.dto.CartDto
import com.example.hhplus_ecommerce.infrastructure.cart.entity.Cart

interface CartRepository {
	fun insert(cartDto: CartDto): Cart

	fun deleteByUserIdAndProductId(userId: Long, productDetailId: Long): Cart

	fun getAllByUserId(userId: Long): List<Cart>
}