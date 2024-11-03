package com.example.hhplus_ecommerce.infrastructure.message.consumer

import com.example.hhplus_ecommerce.domain.product.ProductService
import com.example.hhplus_ecommerce.infrastructure.message.producer.dto.ProductMessage
import com.example.hhplus_ecommerce.infrastructure.message.topic.PRODUCT_ORDER_TOPIC
import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class KafkaConsumer(
	private val productService: ProductService
) {
	private val logger = KotlinLogging.logger {}

	@KafkaListener(groupId = "\${spring.kafka.consumer.group-id}", topics = [PRODUCT_ORDER_TOPIC])
	fun listenProductOrderEvent(@Payload message: ProductMessage) {
		productService.updateProductQuantityDecrease(message.productDetailId, message.orderQuantity)
	}
}