package com.example.hhplus_ecommerce.cart

import com.example.hhplus_ecommerce.domain.cart.dto.CartDto
import com.example.hhplus_ecommerce.infrastructure.cart.entity.CartEntity

interface CartRepository {
	fun insert(cartDto: CartDto): CartEntity

	fun deleteByUserIdAndProductId(userId: Long, productDetailId: Long): CartEntity

	fun deleteAllByUserId(userId: Long): List<CartEntity>

	fun getAllByUserId(userId: Long): List<CartEntity>
}