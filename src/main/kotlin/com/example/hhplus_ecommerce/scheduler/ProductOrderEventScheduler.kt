package com.example.hhplus_ecommerce.scheduler

import com.example.hhplus_ecommerce.domain.outbox.OutboxEventStatus
import com.example.hhplus_ecommerce.domain.outbox.ProductOrderEventOutboxService
import com.example.hhplus_ecommerce.domain.outbox.dto.ProductOrderEventOutboxRequestDto
import com.example.hhplus_ecommerce.infrastructure.kafka.producer.MessageProducer
import com.example.hhplus_ecommerce.infrastructure.kafka.producer.dto.ProductMessage
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ProductOrderEventScheduler(
	private val productOrderEventOutboxService: ProductOrderEventOutboxService,
	private val messageProducer: MessageProducer
) {
	/**
	 * 매 5분마다 INIT 상태인 이벤트 재시도
	 */
	@Scheduled(cron = "0 */5 * * * *")
	fun retryOrderEventInitStatus() {
		val initEventOutboxes = productOrderEventOutboxService.getAllByEventStatus(OutboxEventStatus.INIT)
			.filter { it.createdDate < LocalDateTime.now().minusMinutes(5) }   // 메시지가 정상적으로 발행되고 소비될 때까지 보장 시간 5분

		initEventOutboxes.forEach {
			productOrderEventOutboxService.updateEventStatusPublish(
				ProductOrderEventOutboxRequestDto(it.userId, it.productDetailId, it.orderQuantity)
			)

			messageProducer.sendProductOrderMessage(
				ProductMessage(it.userId, it.productDetailId, it.orderQuantity)
			)
		}
	}

	/**
	 * 매 5분마다 PUBLISH 상태인 이벤트 재시도
	 */
	@Scheduled(cron = "0 */5 * * * *")
	fun retryOrderEventPublishStatus() {
		val publishEventOutboxes = productOrderEventOutboxService.getAllByEventStatus(OutboxEventStatus.PUBLISH)
			.filter { it.createdDate < LocalDateTime.now().minusMinutes(5) }

		publishEventOutboxes.forEach {
			messageProducer.sendProductOrderMessage(
				ProductMessage(it.userId, it.productDetailId, it.orderQuantity)
			)
		}
	}
}