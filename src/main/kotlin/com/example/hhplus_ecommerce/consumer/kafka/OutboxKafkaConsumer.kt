package com.example.hhplus_ecommerce.consumer.kafka

import com.example.hhplus_ecommerce.domain.outbox.PaymentEventOutboxService
import com.example.hhplus_ecommerce.domain.outbox.dto.PaymentEventOutboxRequestDto
import com.example.hhplus_ecommerce.infrastructure.kafka.producer.dto.PaymentDataMessage
import com.example.hhplus_ecommerce.infrastructure.kafka.topic.AFTER_PAYMENT_TOPIC
import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class OutboxKafkaConsumer(
	private val paymentEventOutboxService: PaymentEventOutboxService
) {
	private val logger = KotlinLogging.logger {}

	/**
	 * OUTBOX COMPLETE 상태 업데이트용 컨슈머
	 */
	@KafkaListener(groupId = "outbox_group", topics = [AFTER_PAYMENT_TOPIC])
	fun listenPaymentDataPlatformEvent(@Payload message: PaymentDataMessage) {
		logger.info("OUTBOX CONSUMER GROUP: After Payment Topic - userId: {}, orderId: {}", message.userId, message.orderId)
		paymentEventOutboxService.updateEventStatusComplete(
			PaymentEventOutboxRequestDto(message.userId, message.orderId)
		)
	}
}