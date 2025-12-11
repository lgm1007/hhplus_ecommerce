package com.example.hhplus_ecommerce.infrastructure.order.entity

import com.example.hhplus_ecommerce.domain.order.OrderStatus
import com.example.hhplus_ecommerce.domain.order.dto.OrderDto
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "ORDERTABLE")
class OrderTableEntity(
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
		fun from(orderDto: OrderDto): OrderTableEntity {
			return OrderTableEntity(
				orderDto.userId,
				orderDto.orderDate,
				orderDto.totalPrice,
				orderDto.orderStatus
			)
		}
	}
}