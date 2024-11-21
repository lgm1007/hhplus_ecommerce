package com.example.hhplus_ecommerce.consumer.kafka

import com.example.hhplus_ecommerce.domain.messaging.producer.dto.PaymentDataMessage
import com.example.hhplus_ecommerce.domain.messaging.producer.dto.ProductMessage
import com.example.hhplus_ecommerce.domain.product.ProductService
import com.example.hhplus_ecommerce.infrastructure.external.ExternalDataPlatform
import com.example.hhplus_ecommerce.infrastructure.kafka.topic.AFTER_PAYMENT_TOPIC
import com.example.hhplus_ecommerce.infrastructure.kafka.topic.PRODUCT_ORDER_TOPIC
import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class EcommerceKafkaConsumer(
	private val productService: ProductService
) {
	private val logger = KotlinLogging.logger {}

	@KafkaListener(groupId = "\${spring.kafka.consumer.group-id}", topics = [PRODUCT_ORDER_TOPIC])
	fun listenProductOrderEvent(@Payload message: ProductMessage) {
		productService.updateProductQuantityDecrease(message.productDetailId, message.orderQuantity)
	}

	@KafkaListener(groupId = "\${spring.kafka.consumer.group-id}", topics = [AFTER_PAYMENT_TOPIC])
	fun listenAfterPaymentEvent(@Payload message: PaymentDataMessage) {
		val dataPlatform = ExternalDataPlatform()
		dataPlatform.sendPaymentData(message.orderId, message.currentBalance, message.paymentDate)
	}
}