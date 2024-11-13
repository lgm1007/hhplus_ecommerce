package com.example.hhplus_ecommerce.infrastructure.message.producer

import com.example.hhplus_ecommerce.infrastructure.message.producer.dto.PaymentDataMessage
import com.example.hhplus_ecommerce.infrastructure.message.producer.dto.ProductMessage
import com.example.hhplus_ecommerce.infrastructure.message.topic.DATA_PLATFORM_TOPIC
import com.example.hhplus_ecommerce.infrastructure.message.topic.PRODUCT_ORDER_TOPIC
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaProducer(
	private val kafkaTemplate: KafkaTemplate<String, Any>
) : MessageProducer {
	override fun sendProductOrderMessage(message: ProductMessage) {
		kafkaTemplate.send(PRODUCT_ORDER_TOPIC, message.productDetailId.toString(), message)
	}

	override fun sendPaymentDataPlatformMessage(message: PaymentDataMessage) {
		kafkaTemplate.send(DATA_PLATFORM_TOPIC, message.paymentId.toString(), message)
	}
}