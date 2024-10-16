package com.example.hhplus_ecommerce.infrastructure.cart

import com.example.hhplus_ecommerce.api.error.ErrorStatus
import com.example.hhplus_ecommerce.domain.cart.CartRepository
import com.example.hhplus_ecommerce.domain.cart.dto.CartDto
import com.example.hhplus_ecommerce.exception.NotFoundException
import com.example.hhplus_ecommerce.infrastructure.cart.entity.Cart
import org.springframework.stereotype.Repository

@Repository
class CartRepositoryImpl(private val cartJpaRepository: CartJpaRepository) : CartRepository {
	override fun insert(cartDto: CartDto): Cart {
		return cartJpaRepository.save(Cart.from(cartDto))
	}

	override fun deleteByUserIdAndProductId(userId: Long, productDetailId: Long): Cart {
		val cart = (cartJpaRepository.findByUserIdAndProductDetailId(userId, productDetailId)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_CART))

		cartJpaRepository.delete(cart)

		return cart
	}

	override fun getAllByUserId(userId: Long): List<Cart> {
		return cartJpaRepository.findAllByUserId(userId)
	}
}