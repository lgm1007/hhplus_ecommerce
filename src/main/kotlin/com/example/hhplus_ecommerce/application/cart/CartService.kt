package com.example.hhplus_ecommerce.application.cart

import com.example.hhplus_ecommerce.domain.cart.CartRepository
import com.example.hhplus_ecommerce.domain.cart.dto.CartDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CartService(private val cartRepository: CartRepository) {
	@Transactional
	fun addProductCart(cartDto: CartDto): CartDto {
		return CartDto.Companion.from(cartRepository.insert(cartDto))
	}

	@Transactional
	fun deleteCartByUserProduct(userId: Long, productDetailId: Long): CartDto {
		return CartDto.Companion.from(cartRepository.deleteByUserIdAndProductId(userId, productDetailId))
	}

	@Transactional
	fun deleteCartByUser(userId: Long): List<CartDto> {
		return CartDto.Companion.fromList(cartRepository.deleteAllByUserId(userId))
	}

	@Transactional(readOnly = true)
	fun getAllCartsByUser(userId: Long): List<CartDto> {
		return CartDto.Companion.fromList(cartRepository.getAllByUserId(userId))
	}
}