package com.example.hhplus_ecommerce.event

import com.example.hhplus_ecommerce.infrastructure.external.ExternalDataPlatform
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class DataPlatformEventListener {
    @Async
    @EventListener
    fun listen(event: DataPlatformEvent) {
        // 외부 데이터 플랫폼에 전송하는 동작
        val dataPlatform = ExternalDataPlatform()
        dataPlatform.sendPaymentData(event.paymentResultInfo)
    }
}