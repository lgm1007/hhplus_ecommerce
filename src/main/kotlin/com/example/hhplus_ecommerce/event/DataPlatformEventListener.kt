package com.example.hhplus_ecommerce.event

import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class DataPlatformEventListener {
    @Async
    @EventListener
    fun listen(event: DataPlatformEvent) {
        println("외부 데이터 플랫폼에 전송 성공. " +
                "[paymentId = ${event.paymentResultInfo.paymentId}, orderId = ${event.paymentResultInfo.orderId}, 결제 일자 = ${event.paymentResultInfo.paymentDate}]")
    }
}