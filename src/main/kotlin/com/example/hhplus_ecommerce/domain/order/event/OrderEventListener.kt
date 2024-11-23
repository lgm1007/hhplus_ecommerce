package com.example.hhplus_ecommerce.domain.order.event

import com.example.hhplus_ecommerce.domain.messaging.producer.MessageProducer
import com.example.hhplus_ecommerce.domain.messaging.producer.dto.ProductMessage
import com.example.hhplus_ecommerce.domain.outbox.OutboxEventStatus
import com.example.hhplus_ecommerce.domain.outbox.ProductOrderEventOutboxService
import com.example.hhplus_ecommerce.domain.outbox.dto.ProductOrderEventOutboxDto
import com.example.hhplus_ecommerce.domain.outbox.dto.ProductOrderEventOutboxRequestDto
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import java.time.LocalDateTime

@Component
class OrderEventListener(
	private val productOrderEventOutboxService: ProductOrderEventOutboxService,
	private val messageProducer: MessageProducer
) {
	/**
	 * 주문 트랜잭션 커밋 전 이벤트 발행
	 * outbox 테이블에 INIT 상태인 데이터 저장
	 */
	@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
	fun listenBeforeProductOrder(event: BeforeProductOrderEvent) {
		val userId = event.orderEventInfo.userId
		val orderItemInfos = event.orderEventInfo.orderItemInfos

		val productOrderEventOutboxDtos = orderItemInfos.map {
			ProductOrderEventOutboxDto(
				0,
				userId,
				it.productDetailId,
				it.quantity,
				OutboxEventStatus.INIT,
				LocalDateTime.now()
			)
		}.toList()

		productOrderEventOutboxService.saveAll(productOrderEventOutboxDtos)
	}

	/**
	 * 주문 트랜잭션 커밋 후 이벤트 발행
	 * outbox 테이블 PUBLISH 상태 업데이트 및 Kafka 메시지 발행
	 */
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	fun listenProductOrderEvent(event: ProductOrderMessageEvent) {
		val userId = event.orderEventInfo.userId
		val orderItemInfos = event.orderEventInfo.orderItemInfos

		// outbox 데이터 상태 PUBLISH 로 업데이트
		for (orderItemInfo in orderItemInfos) {
			productOrderEventOutboxService.updateEventStatusPublish(
				ProductOrderEventOutboxRequestDto(userId, orderItemInfo.productDetailId, orderItemInfo.quantity)
			)
		}

		// 주문한 상품 별 재고 차감 메시지 발행
		for (orderItemInfo in orderItemInfos) {
			messageProducer.sendProductOrderMessage(
				ProductMessage(
					userId,
					orderItemInfo.productDetailId,
					orderItemInfo.quantity
				)
			)
		}
	}
}