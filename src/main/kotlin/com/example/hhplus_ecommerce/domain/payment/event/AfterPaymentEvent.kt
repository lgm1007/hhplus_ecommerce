package com.example.hhplus_ecommerce.domain.payment.event

import com.example.hhplus_ecommerce.domain.payment.dto.AfterPaymentEventInfo
import org.springframework.context.ApplicationEvent

class AfterPaymentEvent(
    val paymentEventInfo: AfterPaymentEventInfo
) : ApplicationEvent(paymentEventInfo)