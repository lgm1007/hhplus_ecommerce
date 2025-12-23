package com.example.hhplus_ecommerce.consumer.kafka

import com.example.hhplus_ecommerce.kafka.topic.AFTER_PAYMENT_TOPIC
import com.example.hhplus_ecommerce.kafka.topic.PRODUCT_ORDER_TOPIC
import com.example.hhplus_ecommerce.messaging.producer.PaymentDataMessage
import com.example.hhplus_ecommerce.messaging.producer.ProductMessage
import com.example.hhplus_ecommerce.outbox.OutboxEventStatus
import com.example.hhplus_ecommerce.outbox.PaymentEventOutboxService
import com.example.hhplus_ecommerce.outbox.ProductOrderEventOutboxService
import com.example.hhplus_ecommerce.outbox.dto.PaymentEventOutboxRequestDto
import com.example.hhplus_ecommerce.outbox.dto.ProductOrderEventOutboxRequestDto
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
		paymentEventOutboxService.updateEventStatus(
			PaymentEventOutboxRequestDto(
				userId = message.userId,
				orderId = message.orderId,
				eventStatus = OutboxEventStatus.COMPLETE
			)
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
		productOrderEventOutboxService.updateEventStatus(
			ProductOrderEventOutboxRequestDto(
				userId = message.userId,
				productDetailId = message.productDetailId,
				orderQuantity = message.orderQuantity,
				eventStatus = OutboxEventStatus.COMPLETE
			)
		)
	}
}