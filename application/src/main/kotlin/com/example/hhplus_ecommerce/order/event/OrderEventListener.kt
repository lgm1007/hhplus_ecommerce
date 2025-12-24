package com.example.hhplus_ecommerce.order.event

import com.example.hhplus_ecommerce.messaging.producer.MessageProducer
import com.example.hhplus_ecommerce.messaging.producer.ProductMessage
import com.example.hhplus_ecommerce.outbox.OutboxEventStatus
import com.example.hhplus_ecommerce.outbox.ProductOrderEventOutboxService
import com.example.hhplus_ecommerce.outbox.dto.ProductOrderEventOutboxDto
import com.example.hhplus_ecommerce.outbox.dto.ProductOrderEventOutboxRequestDto
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
		val orderItemInfos = event.orderEventInfo.orderItemInfoDtos

		val productOrderEventOutboxDtos = orderItemInfos.map {
			ProductOrderEventOutboxDto(
				productOrderEventOutboxId = 0,
				userId = userId,
				productDetailId = it.productDetailId,
				orderQuantity = it.quantity,
				eventStatus = OutboxEventStatus.INIT,
				createdDate = LocalDateTime.now()
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
		val orderItemInfos = event.orderEventInfo.orderItemInfoDtos

		// outbox 데이터 상태 PUBLISH 로 업데이트
		for (orderItemInfo in orderItemInfos) {
			productOrderEventOutboxService.updateEventStatus(
				ProductOrderEventOutboxRequestDto(
					userId = userId,
					productDetailId = orderItemInfo.productDetailId,
					orderQuantity = orderItemInfo.quantity,
					eventStatus = OutboxEventStatus.PUBLISH
				)
			)
		}

		// 주문한 상품 별 재고 차감 메시지 발행
		for (orderItemInfo in orderItemInfos) {
			messageProducer.sendProductOrderMessage(
				ProductMessage(
					userId = userId,
					productDetailId = orderItemInfo.productDetailId,
					orderQuantity = orderItemInfo.quantity
				)
			)
		}
	}
}