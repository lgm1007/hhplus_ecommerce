package com.example.hhplus_ecommerce.cart

import com.example.hhplus_ecommerce.cart.dto.CartDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CartService(private val cartRepository: CartRepository) {
	@Transactional
	fun addProductCart(cartDto: CartDto): CartDto {
		return CartDto.from(
			cartRepository.insert(
				cart = Cart(
					userId = cartDto.userId,
					productDetailId = cartDto.productDetailId,
					quantity = cartDto.quantity
				)
			)
		)
	}

	@Transactional
	fun deleteCartByUserProduct(userId: Long, productDetailId: Long): CartDto {
		return CartDto.from(cartRepository.deleteByUserIdAndProductId(userId, productDetailId))
	}

	@Transactional
	fun deleteCartByUser(userId: Long): List<CartDto> {
		return CartDto.fromList(cartRepository.deleteAllByUserId(userId))
	}

	@Transactional(readOnly = true)
	fun getAllCartsByUser(userId: Long): List<CartDto> {
		return CartDto.fromList(cartRepository.getAllByUserId(userId))
	}
}