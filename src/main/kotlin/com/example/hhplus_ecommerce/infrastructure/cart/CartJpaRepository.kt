package com.example.hhplus_ecommerce.infrastructure.cart

import com.example.hhplus_ecommerce.infrastructure.cart.entity.CartEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CartJpaRepository : JpaRepository<CartEntity, Long> {
	fun findByUserIdAndProductDetailId(userId: Long, productDetailId: Long): CartEntity?

	fun findAllByUserId(userId: Long): List<CartEntity>
}