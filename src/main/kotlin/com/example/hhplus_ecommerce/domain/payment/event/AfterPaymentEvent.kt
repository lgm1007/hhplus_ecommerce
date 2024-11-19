package com.example.hhplus_ecommerce.domain.payment.event

import com.example.hhplus_ecommerce.domain.payment.dto.PaymentResultInfo
import org.springframework.context.ApplicationEvent

class AfterPaymentEvent(
    val paymentResultInfo: PaymentResultInfo
) : ApplicationEvent(paymentResultInfo)