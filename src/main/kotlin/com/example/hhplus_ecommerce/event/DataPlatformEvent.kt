package com.example.hhplus_ecommerce.event

import com.example.hhplus_ecommerce.domain.payment.dto.PaymentResultInfo
import org.springframework.context.ApplicationEvent

class DataPlatformEvent(
    val paymentResultInfo: PaymentResultInfo
) : ApplicationEvent(paymentResultInfo)