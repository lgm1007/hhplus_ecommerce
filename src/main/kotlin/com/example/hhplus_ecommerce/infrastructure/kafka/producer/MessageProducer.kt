package com.example.hhplus_ecommerce.infrastructure.kafka.producer

import com.example.hhplus_ecommerce.infrastructure.kafka.producer.dto.PaymentDataMessage
import com.example.hhplus_ecommerce.infrastructure.kafka.producer.dto.ProductMessage

interface MessageProducer {
	fun sendProductOrderMessage(message: ProductMessage)

	fun sendAfterPaymentMessage(message: PaymentDataMessage)
}