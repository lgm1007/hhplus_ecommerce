package com.example.hhplus_ecommerce.cart.entity

import com.example.hhplus_ecommerce.cart.Cart
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.Table

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
		fun from(cart: Cart): CartEntity {
			return CartEntity(
				userId = cart.userId,
				productDetailId = cart.productDetailId,
				quantity = cart.quantity
			)
		}
	}

	fun toDomain(): Cart {
		return Cart(
			cartId = this.id,
			userId = this.userId,
			productDetailId = this.productDetailId,
			quantity = this.quantity,
			createdDate = this.createdDate
		)
	}
}