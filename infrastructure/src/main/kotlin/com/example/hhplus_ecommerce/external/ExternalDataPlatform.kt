package com.example.hhplus_ecommerce.external

import com.example.hhplus_ecommerce.share.exception.ExternalRequestException
import java.time.LocalDateTime

class ExternalDataPlatform {
	fun sendPaymentData(orderId: Long, currentBalance: Int, paymentDate: LocalDateTime) {
		// 외부 플랫폼 요청 중 예외 발생했다고 가정
		throw ExternalRequestException("Error:: 외부 데이터 플랫폼에 전송 중 예외 발생")
	}
}