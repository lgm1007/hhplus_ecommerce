package com.example.hhplus_ecommerce.payment.event

import com.example.hhplus_ecommerce.payment.dto.AfterPaymentEventInfoDto
import org.springframework.context.ApplicationEvent

class AfterPaymentEvent(
    val paymentEventInfoDto: AfterPaymentEventInfoDto
) : ApplicationEvent(paymentEventInfoDto)