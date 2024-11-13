package com.example.hhplus_ecommerce.infrastructure.message.producer

import com.example.hhplus_ecommerce.infrastructure.message.producer.dto.PaymentDataMessage
import com.example.hhplus_ecommerce.infrastructure.message.producer.dto.ProductMessage

interface MessageProducer {
	fun sendProductOrderMessage(message: ProductMessage)

	fun sendPaymentDataPlatformMessage(message: PaymentDataMessage)
}