package com.example.hhplus_ecommerce.payment

import com.example.hhplus_ecommerce.payment.dto.PaymentDto
import org.springframework.stereotype.Service

@Service
class PaymentService(private val paymentRepository: PaymentRepository) {
	fun registerPayment(paymentDto: PaymentDto): PaymentDto {
		return PaymentDto.from(
			paymentRepository.insert(
				Payment(
					userId = paymentDto.userId,
					orderId = paymentDto.orderId,
					price = paymentDto.price,
					paymentStatus = paymentDto.paymentStatus
				)
			)
		)
	}
}