package com.example.hhplus_ecommerce.payment.event

import com.example.hhplus_ecommerce.messaging.producer.MessageProducer
import com.example.hhplus_ecommerce.messaging.producer.PaymentDataMessage
import com.example.hhplus_ecommerce.outbox.OutboxEventStatus
import com.example.hhplus_ecommerce.outbox.PaymentEventOutboxService
import com.example.hhplus_ecommerce.outbox.dto.PaymentEventOutboxDto
import com.example.hhplus_ecommerce.outbox.dto.PaymentEventOutboxRequestDto
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import java.time.LocalDateTime

@Component
class PaymentEventListener(
    private val messageProducer: MessageProducer,
    private val paymentEventOutboxService: PaymentEventOutboxService,
) {
    /**
     * 결제 트랜잭션 커밋 전 이벤트 발행
     * outbox 테이블에 INIT 상태인 데이터 저장
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun listenBeforePaymentEvent(event: BeforePaymentEvent) {
        paymentEventOutboxService.save(
            PaymentEventOutboxDto(
                paymentEventOutboxId = 0L,
                userId = event.paymentEventRequest.userId,
                orderId = event.paymentEventRequest.orderId,
                eventStatus = OutboxEventStatus.INIT,
                createdDate = LocalDateTime.now()
            )
        )
    }

    /**
     * 결제 트랜잭션 커밋 후 이벤트 발행
     * outbox 테이블 PUBLISH 상태 업데이트 및 Kafka 메시지 발행
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun listenAfterPaymentEvent(event: AfterPaymentEvent) {
        paymentEventOutboxService.updateEventStatus(
            PaymentEventOutboxRequestDto(
                userId = event.paymentEventInfoDto.userId,
                orderId = event.paymentEventInfoDto.orderId,
                eventStatus = OutboxEventStatus.PUBLISH
            )
        )
        // 외부 데이터 플랫폼에 전송하는 이벤트 발행
        messageProducer.sendAfterPaymentMessage(
            PaymentDataMessage(
                userId = event.paymentEventInfoDto.userId,
                orderId = event.paymentEventInfoDto.orderId,
                currentBalance = event.paymentEventInfoDto.currentBalance,
                paymentDate = event.paymentEventInfoDto.paymentDate
            )
        )
    }
}