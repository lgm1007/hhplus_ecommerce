package com.example.hhplus_ecommerce.application.consumer.kafka

import com.example.hhplus_ecommerce.domain.messaging.producer.dto.PaymentDataMessage
import com.example.hhplus_ecommerce.domain.messaging.producer.dto.ProductMessage
import com.example.hhplus_ecommerce.domain.outbox.PaymentEventOutboxService
import com.example.hhplus_ecommerce.domain.outbox.ProductOrderEventOutboxService
import com.example.hhplus_ecommerce.domain.outbox.dto.PaymentEventOutboxRequestDto
import com.example.hhplus_ecommerce.domain.outbox.dto.ProductOrderEventOutboxRequestDto
import com.example.hhplus_ecommerce.infrastructure.kafka.topic.AFTER_PAYMENT_TOPIC
import com.example.hhplus_ecommerce.infrastructure.kafka.topic.PRODUCT_ORDER_TOPIC
import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

/**
 * OUTBOX COMPLETE 상태 업데이트용 컨슈머
 */
@Component
class OutboxKafkaConsumer(
	private val paymentEventOutboxService: PaymentEventOutboxService,
	private val productOrderEventOutboxService: ProductOrderEventOutboxService
) {
	private val logger = KotlinLogging.logger {}

	@KafkaListener(groupId = "outbox_group", topics = [AFTER_PAYMENT_TOPIC])
	fun listenAfterPaymentEvent(@Payload message: PaymentDataMessage) {
		logger.info(
			"OUTBOX CONSUMER GROUP: After Payment Topic - userId: {}, orderId: {}",
			message.userId,
			message.orderId
		)
		paymentEventOutboxService.updateEventStatusComplete(
			PaymentEventOutboxRequestDto(message.userId, message.orderId)
		)
	}

	@KafkaListener(groupId = "outbox_group", topics = [PRODUCT_ORDER_TOPIC])
	fun listenProductOrderEvent(@Payload message: ProductMessage) {
		logger.info(
			"OUTBOX CONSUMER GROUP: Product Order Topic - userId: {}, productDetailId: {}, orderQuantity: {}",
			message.userId,
			message.productDetailId,
			message.orderQuantity
		)
		productOrderEventOutboxService.updateEventStatusComplete(
			ProductOrderEventOutboxRequestDto(message.userId, message.productDetailId, message.orderQuantity)
		)
	}
}