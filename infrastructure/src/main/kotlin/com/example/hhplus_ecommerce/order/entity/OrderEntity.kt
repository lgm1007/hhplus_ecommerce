package com.example.hhplus_ecommerce.order.entity

import com.example.hhplus_ecommerce.order.Order
import com.example.hhplus_ecommerce.order.OrderStatus
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
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
@Table(name = "ORDERTABLE")
class OrderEntity(
	val userId: Long,
	val orderDate: LocalDateTime,
	val totalPrice: Int,
	var orderStatus: OrderStatus,
) {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0

	@CreatedDate
	var createdDate: LocalDateTime = LocalDateTime.now()
		private set

	@LastModifiedDate
	var lastModifiedDate: LocalDateTime = LocalDateTime.now()
		private set

	fun updateStatus(orderStatus: OrderStatus) {
		this.orderStatus = orderStatus
	}

	companion object {
		fun from(order: Order): OrderEntity {
			return OrderEntity(
				userId = order.userId,
				orderDate = order.orderDate,
				totalPrice = order.totalPrice,
				orderStatus = order.orderStatus
			)
		}
	}

	fun toDomain(): Order {
		return Order(
			orderId = this.id,
			userId = this.userId,
			orderDate = this.orderDate,
			totalPrice = this.totalPrice,
			orderStatus = this.orderStatus,
			createdDate = this.createdDate,
			lastModifiedDate = this.lastModifiedDate
		)
	}
}