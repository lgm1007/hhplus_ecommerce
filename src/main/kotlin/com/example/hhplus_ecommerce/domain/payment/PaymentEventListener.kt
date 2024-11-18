package com.example.hhplus_ecommerce.domain.payment

import com.example.hhplus_ecommerce.event.DataPlatformEvent
import com.example.hhplus_ecommerce.infrastructure.message.producer.MessageProducer
import com.example.hhplus_ecommerce.infrastructure.message.producer.dto.PaymentDataMessage
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class PaymentEventListener(
    private val messageProducer: MessageProducer
) {
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun listen(event: DataPlatformEvent) {
        // 외부 데이터 플랫폼에 전송하는 이벤트 발행
        messageProducer.sendPaymentDataPlatformMessage(PaymentDataMessage.from(event.paymentResultInfo))
    }
}