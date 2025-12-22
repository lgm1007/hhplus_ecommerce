package com.example.hhplus_ecommerce.order.entity

import com.example.hhplus_ecommerce.order.OrderItem
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "ORDERITEM")
class OrderItemEntity(
	val orderId: Long,
	val productDetailId: Long,
	val quantity: Int,
	val price: Int,
) {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0

	@CreatedDate
	var createdDate: LocalDateTime = LocalDateTime.now()
		private set

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