package com.example.hhplus_ecommerce.infrastructure.external

import com.example.hhplus_ecommerce.domain.payment.dto.PaymentResultInfo

class ExternalDataPlatform {
	fun sendPaymentData(paymentResultInfo: PaymentResultInfo) {
		println("외부 데이터 플랫폼에 전송 성공. " +
			"[paymentId = ${paymentResultInfo.paymentId}, orderId = ${paymentResultInfo.orderId}, 결제 일자 = ${paymentResultInfo.paymentDate}]")
	}
}