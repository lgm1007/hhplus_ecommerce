package com.example.hhplus_ecommerce.infrastructure.cart

import com.example.hhplus_ecommerce.infrastructure.cart.entity.Cart
import org.springframework.data.jpa.repository.JpaRepository

interface CartJpaRepository : JpaRepository<Cart, Long> {
	fun findByUserIdAndProductDetailId(userId: Long, productDetailId: Long): Cart?

	fun findAllByUserId(userId: Long): List<Cart>
}