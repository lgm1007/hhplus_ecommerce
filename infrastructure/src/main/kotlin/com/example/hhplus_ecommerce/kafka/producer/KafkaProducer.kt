package com.example.hhplus_ecommerce.kafka.producer

import com.example.hhplus_ecommerce.kafka.topic.AFTER_PAYMENT_TOPIC
import com.example.hhplus_ecommerce.kafka.topic.PRODUCT_ORDER_TOPIC
import com.example.hhplus_ecommerce.messaging.producer.MessageProducer
import com.example.hhplus_ecommerce.messaging.producer.PaymentDataMessage
import com.example.hhplus_ecommerce.messaging.producer.ProductMessage
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