package com.example.hhplus_ecommerce.cart

import com.example.hhplus_ecommerce.cart.entity.CartEntity
import com.example.hhplus_ecommerce.share.exception.ErrorStatus
import com.example.hhplus_ecommerce.share.exception.NotFoundException
import org.springframework.stereotype.Repository

@Repository
class CartRepositoryImpl(private val cartJpaRepository: CartJpaRepository) : CartRepository {
	override fun insert(cart: Cart): Cart {
		return cartJpaRepository.save(CartEntity.from(cart))
			.toDomain()
	}

	override fun deleteByUserIdAndProductId(userId: Long, productDetailId: Long): Cart {
		val findEntity = (cartJpaRepository.findByUserIdAndProductDetailId(userId, productDetailId)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_CART))

		cartJpaRepository.delete(findEntity)

		return findEntity.toDomain()
	}

	override fun deleteAllByUserId(userId: Long): List<Cart> {
		val findEntities = cartJpaRepository.findAllByUserId(userId)

		cartJpaRepository.deleteAll(findEntities)

		return findEntities.map { it.toDomain() }
	}

	override fun getAllByUserId(userId: Long): List<Cart> {
		val findEntities = cartJpaRepository.findAllByUserId(userId)

		if (findEntities.isEmpty()) {
			throw NotFoundException(ErrorStatus.NOT_FOUND_CART)
		}

		return findEntities.map { it.toDomain() }
	}
}