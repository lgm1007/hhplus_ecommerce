package com.example.hhplus_ecommerce.infrastructure.order.entity

import com.example.hhplus_ecommerce.domain.order.dto.OrderItemDto
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
class OrderItem(
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
		fun from(orderItemDto: OrderItemDto): OrderItem {
			return OrderItem(
				orderItemDto.orderId,
				orderItemDto.productDetailId,
				orderItemDto.quantity,
				orderItemDto.price
			)
		}
	}
}