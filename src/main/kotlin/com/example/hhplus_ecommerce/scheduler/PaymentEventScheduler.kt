package com.example.hhplus_ecommerce.scheduler

import com.example.hhplus_ecommerce.domain.balance.BalanceService
import com.example.hhplus_ecommerce.domain.outbox.OutboxEventStatus
import com.example.hhplus_ecommerce.domain.outbox.PaymentEventOutboxService
import com.example.hhplus_ecommerce.domain.outbox.dto.PaymentEventOutboxRequestDto
import com.example.hhplus_ecommerce.infrastructure.kafka.producer.MessageProducer
import com.example.hhplus_ecommerce.infrastructure.kafka.producer.dto.PaymentDataMessage
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class PaymentEventScheduler(
	private val paymentEventOutboxService: PaymentEventOutboxService,
	private val balanceService: BalanceService,
	private val messageProducer: MessageProducer
) {
	/**
	 * 매 5분마다 INIT 상태인 이벤트 재시도
	 */
	@Scheduled(cron = "0 */5 * * * *")
	fun retryPaymentEventInitStatus() {
		val initEventOutboxes = paymentEventOutboxService.getAllByEventStatus(OutboxEventStatus.INIT)
			.filter { it.createdDate < LocalDateTime.now().minusMinutes(5) }   // 메시지가 정상적으로 발행되고 소비될 때까지 보장 시간 5분

		initEventOutboxes.forEach {
			val currentBalance = balanceService.getByUserId(it.userId).amount

			paymentEventOutboxService.updateEventStatusPublish(
				PaymentEventOutboxRequestDto(it.userId, it.orderId)
			)

			messageProducer.sendAfterPaymentMessage(
				PaymentDataMessage(it.userId, it.orderId, currentBalance, it.createdDate)
			)
		}
	}

	/**
	 * 매 5분마다 PUBLISH 상태인 이벤트 재시도
	 */
	@Scheduled(cron = "0 */5 * * * *")
	fun retryPaymentEventPublishStatus() {
		val publishEventOutboxes = paymentEventOutboxService.getAllByEventStatus(OutboxEventStatus.PUBLISH)
			.filter { it.createdDate < LocalDateTime.now().minusMinutes(5) }

		publishEventOutboxes.forEach {
			val currentBalance = balanceService.getByUserId(it.userId).amount

			messageProducer.sendAfterPaymentMessage(
				PaymentDataMessage(it.userId, it.orderId, currentBalance, it.createdDate)
			)
		}
	}

	/**
	 * 하루에 한 번 COMPLETE 상태이며 저장된 지 한 달 된 메시지 삭제
	 */
	@Scheduled(cron = "0 0 0 * * *")
	fun deletePaymentEventCompleteBeforeMonth() {
		val completeEventOutboxes = paymentEventOutboxService.getAllByEventStatus(OutboxEventStatus.COMPLETE)
			.filter { it.createdDate < LocalDateTime.now().minusMonths(1) }

		completeEventOutboxes.forEach {
			paymentEventOutboxService.deleteById(it.id)
		}
	}
}