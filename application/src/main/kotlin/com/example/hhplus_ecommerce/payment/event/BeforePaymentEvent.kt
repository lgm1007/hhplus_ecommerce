package com.example.hhplus_ecommerce.payment.event

import com.example.hhplus_ecommerce.payment.dto.PaymentEventRequestInfoDto
import org.springframework.context.ApplicationEvent

class BeforePaymentEvent(
    val paymentEventRequest: PaymentEventRequestInfoDto
) : ApplicationEvent(paymentEventRequest)