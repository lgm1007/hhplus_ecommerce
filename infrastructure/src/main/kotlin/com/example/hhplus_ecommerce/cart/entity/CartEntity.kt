package com.example.hhplus_ecommerce.cart.entity

import com.example.hhplus_ecommerce.BaseEntity
import com.example.hhplus_ecommerce.cart.Cart
import javax.persistence.Entity
import javax.persistence.Index
import javax.persistence.Table

@Entity
@Table(name = "CART", indexes = [Index(name = "idx_cart_user_id", columnList = "userId")])
class CartEntity(
	val userId: Long,
	val productDetailId: Long,
	val quantity: Int
) : BaseEntity() {
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