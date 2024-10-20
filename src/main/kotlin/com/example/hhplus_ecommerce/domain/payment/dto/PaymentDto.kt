package com.example.hhplus_ecommerce.domain.payment.dto

import com.example.hhplus_ecommerce.domain.order.dto.OrderDto
import com.example.hhplus_ecommerce.domain.payment.PaymentStatus
import com.example.hhplus_ecommerce.infrastructure.payment.entity.Payment
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

		fun from(payment: Payment): PaymentDto {
			return PaymentDto(
				payment.id,
				payment.userId,
				payment.orderId,
				payment.price,
				payment.paymentStatus,
				payment.createdDate,
				payment.lastModifiedDate
			)
		}
	}
}