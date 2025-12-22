package com.example.hhplus_ecommerce.messaging.producer

import com.example.hhplus_ecommerce.domain.messaging.producer.dto.PaymentDataMessage
import com.example.hhplus_ecommerce.domain.messaging.producer.dto.ProductMessage

interface MessageProducer {
	fun sendProductOrderMessage(message: ProductMessage)

	fun sendAfterPaymentMessage(message: PaymentDataMessage)
}