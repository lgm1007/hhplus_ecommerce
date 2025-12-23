package com.example.hhplus_ecommerce.scheduler

import com.example.hhplus_ecommerce.messaging.producer.MessageProducer
import com.example.hhplus_ecommerce.messaging.producer.ProductMessage
import com.example.hhplus_ecommerce.outbox.OutboxEventStatus
import com.example.hhplus_ecommerce.outbox.ProductOrderEventOutboxService
import com.example.hhplus_ecommerce.outbox.dto.ProductOrderEventOutboxRequestDto
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
			.filter { it.createdDate!! < LocalDateTime.now().minusMinutes(5) }   // 메시지가 정상적으로 발행되고 소비될 때까지 보장 시간 5분

		initEventOutboxes.forEach {
			productOrderEventOutboxService.updateEventStatus(
				ProductOrderEventOutboxRequestDto(
					userId = it.userId,
					productDetailId = it.productDetailId,
					orderQuantity = it.orderQuantity,
					eventStatus = OutboxEventStatus.PUBLISH
				)
			)

			messageProducer.sendProductOrderMessage(
				ProductMessage(
					userId = it.userId,
					productDetailId = it.productDetailId,
					orderQuantity = it.orderQuantity
				)
			)
		}
	}

	/**
	 * 매 5분마다 PUBLISH 상태인 이벤트 재시도
	 */
	@Scheduled(cron = "0 */5 * * * *")
	fun retryOrderEventPublishStatus() {
		val publishEventOutboxes = productOrderEventOutboxService.getAllByEventStatus(OutboxEventStatus.PUBLISH)
			.filter { it.createdDate!! < LocalDateTime.now().minusMinutes(5) }

		publishEventOutboxes.forEach {
			messageProducer.sendProductOrderMessage(
				ProductMessage(
					userId = it.userId,
					productDetailId = it.productDetailId,
					orderQuantity = it.orderQuantity
				)
			)
		}
	}

	/**
	 * 하루에 한 번 COMPLETE 상태이며 저장된 지 한 달 된 메시지 삭제
	 */
	@Scheduled(cron = "0 0 0 * * *")
	fun deleteOrderEventCompleteBeforeMonth() {
		val completeEventOutboxes = productOrderEventOutboxService.getAllByEventStatus(OutboxEventStatus.COMPLETE)
			.filter { it.createdDate!! < LocalDateTime.now().minusMonths(1) }

		completeEventOutboxes.forEach {
			productOrderEventOutboxService.deleteById(it.productOrderEventOutboxId!!)
		}
	}
}