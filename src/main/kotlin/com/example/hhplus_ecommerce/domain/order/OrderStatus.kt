package com.example.hhplus_ecommerce.domain.order

enum class OrderStatus(val value: String) {
	ORDER_WAIT("주문대기"),
	ORDER_COMPLETE("주문완료"),
	PAYMENT_COMPLETE("결제완료"),
}