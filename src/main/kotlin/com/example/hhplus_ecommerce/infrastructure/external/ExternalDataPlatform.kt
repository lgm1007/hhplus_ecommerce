package com.example.hhplus_ecommerce.infrastructure.external

import com.example.hhplus_ecommerce.domain.payment.dto.PaymentResultInfo
import com.example.hhplus_ecommerce.exception.ExternalRequestException

class ExternalDataPlatform {
	fun sendPaymentData(paymentResultInfo: PaymentResultInfo) {
		// 외부 플랫폼 요청 중 예외 발생했다고 가정
		try {
			throw ExternalRequestException("Error:: 외부 데이터 플랫폼에 전송 중 예외 발생")
		} catch (e: Exception) {
			println(e.message)
		}
	}
}