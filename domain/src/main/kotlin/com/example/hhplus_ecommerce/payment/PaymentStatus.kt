package com.example.hhplus_ecommerce.payment

enum class PaymentStatus(val message: String) {
	PAYMENT_WAIT("결제대기"),
	PAYMENT_COMPLETE("결제완료"),
}