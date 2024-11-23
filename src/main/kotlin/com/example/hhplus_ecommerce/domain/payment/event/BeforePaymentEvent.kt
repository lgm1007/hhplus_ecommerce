package com.example.hhplus_ecommerce.domain.payment.event

import com.example.hhplus_ecommerce.domain.payment.dto.PaymentEventRequestInfo
import org.springframework.context.ApplicationEvent

class BeforePaymentEvent(
    val paymentEventRequest: PaymentEventRequestInfo
) : ApplicationEvent(paymentEventRequest)