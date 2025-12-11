package com.example.hhplus_ecommerce.infrastructure.cart

import com.example.hhplus_ecommerce.domain.share.exception.ErrorStatus
import com.example.hhplus_ecommerce.domain.cart.CartRepository
import com.example.hhplus_ecommerce.domain.cart.dto.CartDto
import com.example.hhplus_ecommerce.domain.share.exception.NotFoundException
import com.example.hhplus_ecommerce.infrastructure.cart.entity.CartEntity
import org.springframework.stereotype.Repository

@Repository
class CartRepositoryImpl(private val cartJpaRepository: CartJpaRepository) : CartRepository {
	override fun insert(cartDto: CartDto): CartEntity {
		return cartJpaRepository.save(CartEntity.from(cartDto))
	}

	override fun deleteByUserIdAndProductId(userId: Long, productDetailId: Long): CartEntity {
		val cart = (cartJpaRepository.findByUserIdAndProductDetailId(userId, productDetailId)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_CART))

		cartJpaRepository.delete(cart)

		return cart
	}

	override fun deleteAllByUserId(userId: Long): List<CartEntity> {
		val carts = cartJpaRepository.findAllByUserId(userId)

		cartJpaRepository.deleteAll(carts)

		return carts
	}

	override fun getAllByUserId(userId: Long): List<CartEntity> {
		val carts = cartJpaRepository.findAllByUserId(userId)

		if (carts.isEmpty()) {
			throw NotFoundException(ErrorStatus.NOT_FOUND_CART)
		}

		return carts
	}
}