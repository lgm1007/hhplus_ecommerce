package com.example.hhplus_ecommerce.cart

interface CartRepository {
	fun insert(cart: Cart): Cart

	fun deleteByUserIdAndProductId(userId: Long, productDetailId: Long): Cart

	fun deleteAllByUserId(userId: Long): List<Cart>

	fun getAllByUserId(userId: Long): List<Cart>
}