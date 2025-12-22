package com.example.hhplus_ecommerce.payment

interface PaymentRepository {
	fun insert(payment: Payment): Payment
}