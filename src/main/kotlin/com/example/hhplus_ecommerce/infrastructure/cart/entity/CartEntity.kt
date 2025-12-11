package com.example.hhplus_ecommerce.infrastructure.cart.entity

import com.example.hhplus_ecommerce.domain.cart.dto.CartDto
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "CART", indexes = [Index(name = "idx_cart_user_id", columnList = "userId")])
class CartEntity(
	val userId: Long,
	val productDetailId: Long,
	val quantity: Int
) {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0

	@CreatedDate
	var createdDate: LocalDateTime = LocalDateTime.now()
		private set

	companion object {
		fun from(cartDto: CartDto): CartEntity {
			return CartEntity(
				cartDto.userId,
				cartDto.productDetailId,
				cartDto.quantity
			)
		}
	}
}