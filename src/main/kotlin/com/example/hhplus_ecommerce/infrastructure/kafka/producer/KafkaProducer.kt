package com.example.hhplus_ecommerce.infrastructure.kafka.producer

import com.example.hhplus_ecommerce.domain.messaging.producer.MessageProducer
import com.example.hhplus_ecommerce.domain.messaging.producer.dto.PaymentDataMessage
import com.example.hhplus_ecommerce.domain.messaging.producer.dto.ProductMessage
import com.example.hhplus_ecommerce.infrastructure.kafka.topic.AFTER_PAYMENT_TOPIC
import com.example.hhplus_ecommerce.infrastructure.kafka.topic.PRODUCT_ORDER_TOPIC
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaProducer(
	private val kafkaTemplate: KafkaTemplate<String, Any>
) : MessageProducer {
	override fun sendProductOrderMessage(message: ProductMessage) {
		kafkaTemplate.send(PRODUCT_ORDER_TOPIC, message.productDetailId.toString(), message)
	}

	override fun sendAfterPaymentMessage(message: PaymentDataMessage) {
		kafkaTemplate.send(AFTER_PAYMENT_TOPIC, message.userId.toString(), message)
	}
}