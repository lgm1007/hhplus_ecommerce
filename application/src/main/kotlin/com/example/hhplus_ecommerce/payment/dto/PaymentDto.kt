package com.example.hhplus_ecommerce.payment.dto

import com.example.hhplus_ecommerce.order.dto.OrderDto
import com.example.hhplus_ecommerce.payment.Payment
import com.example.hhplus_ecommerce.payment.PaymentStatus
import java.time.LocalDateTime

data class PaymentDto(
	val paymentId: Long? = null,
	val userId: Long,
	val orderId: Long,
	val price: Int,
	var paymentStatus: PaymentStatus,
	val createdDate: LocalDateTime? = null,
	val lastModifiedDate: LocalDateTime? = null
) {
	companion object {
		fun of(userId: Long, orderDto: OrderDto): PaymentDto {
			return PaymentDto(
				paymentId = 0,
				userId = userId,
				orderId = orderDto.orderId!!,
				price = orderDto.totalPrice,
				paymentStatus = PaymentStatus.PAYMENT_COMPLETE,
				createdDate = LocalDateTime.now(),
				lastModifiedDate = LocalDateTime.now()
			)
		}

		fun from(payment: Payment): PaymentDto {
			return PaymentDto(
				paymentId = payment.paymentId,
				userId = payment.userId,
				orderId = payment.orderId,
				price = payment.price,
				paymentStatus = payment.paymentStatus,
				createdDate = payment.createdDate,
				lastModifiedDate = payment.lastModifiedDate
			)
		}
	}
}