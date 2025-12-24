package com.example.hhplus_ecommerce.order.entity

import com.example.hhplus_ecommerce.BaseEntity
import com.example.hhplus_ecommerce.order.OrderItem
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "ORDERITEM")
class OrderItemEntity(
	val orderId: Long,
	val productDetailId: Long,
	val quantity: Int,
	val price: Int,
) : BaseEntity() {
	companion object {
		fun from(orderItem: OrderItem): OrderItemEntity {
			return OrderItemEntity(
				orderId = orderItem.orderId,
				productDetailId = orderItem.productDetailId,
				quantity = orderItem.quantity,
				price = orderItem.price
			)
		}
	}

	fun toDomain(): OrderItem {
		return OrderItem(
			orderItemId = this.id,
			orderId = this.orderId,
			productDetailId = this.productDetailId,
			quantity = this.quantity,
			price = this.price,
			createdDate = this.createdDate
		)
	}
}