package com.example.hhplus_ecommerce.domain.payment.dto

import com.example.hhplus_ecommerce.domain.order.dto.OrderDto
import com.example.hhplus_ecommerce.domain.payment.PaymentStatus
import com.example.hhplus_ecommerce.infrastructure.payment.entity.PaymentEntity
import java.time.LocalDateTime

class PaymentDto(
	val id: Long,
	val userId: Long,
	val orderId: Long,
	val price: Int,
	var paymentStatus: PaymentStatus,
	val createdDate: LocalDateTime,
	val lastModifiedDate: LocalDateTime,
) {
	companion object {
		fun of(userId: Long, orderDto: OrderDto): PaymentDto {
			return PaymentDto(
				0,
				userId,
				orderDto.id,
				orderDto.totalPrice,
				PaymentStatus.PAYMENT_COMPLETE,
				LocalDateTime.now(),
				LocalDateTime.now()
			)
		}

		fun from(paymentEntity: PaymentEntity): PaymentDto {
			return PaymentDto(
				paymentEntity.id,
				paymentEntity.userId,
				paymentEntity.orderId,
				paymentEntity.price,
				paymentEntity.paymentStatus,
				paymentEntity.createdDate,
				paymentEntity.lastModifiedDate
			)
		}
	}
}