package com.example.hhplus_ecommerce.domain.payment.event

import com.example.hhplus_ecommerce.domain.messaging.producer.MessageProducer
import com.example.hhplus_ecommerce.domain.messaging.producer.dto.PaymentDataMessage
import com.example.hhplus_ecommerce.domain.outbox.OutboxEventStatus
import com.example.hhplus_ecommerce.domain.outbox.PaymentEventOutboxService
import com.example.hhplus_ecommerce.domain.outbox.dto.PaymentEventOutboxDto
import com.example.hhplus_ecommerce.domain.outbox.dto.PaymentEventOutboxRequestDto
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
                0L,
                event.paymentEventRequest.userId,
                event.paymentEventRequest.orderId,
                OutboxEventStatus.INIT,
                LocalDateTime.now()
            )
        )
    }

    /**
     * 결제 트랜잭션 커밋 후 이벤트 발행
     * outbox 테이블 PUBLISH 상태 업데이트 및 Kafka 메시지 발행
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun listenAfterPaymentEvent(event: AfterPaymentEvent) {
        paymentEventOutboxService.updateEventStatusPublish(
            PaymentEventOutboxRequestDto(
                event.paymentEventInfo.userId,
                event.paymentEventInfo.orderId
            )
        )
        // 외부 데이터 플랫폼에 전송하는 이벤트 발행
        messageProducer.sendAfterPaymentMessage(PaymentDataMessage.from(event.paymentEventInfo))
    }
}