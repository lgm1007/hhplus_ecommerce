package com.example.hhplus_ecommerce.messaging.producer

interface MessageProducer {
	fun sendProductOrderMessage(message: ProductMessage)

	fun sendAfterPaymentMessage(message: PaymentDataMessage)
}