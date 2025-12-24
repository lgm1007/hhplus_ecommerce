package com.example.hhplus_ecommerce.order.entity

import com.example.hhplus_ecommerce.BaseEntity
import com.example.hhplus_ecommerce.order.Order
import com.example.hhplus_ecommerce.order.OrderStatus
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "ORDERTABLE")
class OrderEntity(
	val userId: Long,
	val orderDate: LocalDateTime,
	val totalPrice: Int,
	var orderStatus: OrderStatus,
) : BaseEntity() {
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