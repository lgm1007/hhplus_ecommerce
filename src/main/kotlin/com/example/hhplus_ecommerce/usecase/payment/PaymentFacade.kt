package com.example.hhplus_ecommerce.usecase.payment

import com.example.hhplus_ecommerce.domain.balance.BalanceService
import com.example.hhplus_ecommerce.domain.order.OrderService
import com.example.hhplus_ecommerce.domain.order.OrderStatus
import com.example.hhplus_ecommerce.domain.payment.PaymentService
import com.example.hhplus_ecommerce.domain.payment.dto.PaymentDto
import com.example.hhplus_ecommerce.domain.payment.dto.PaymentResultInfo
import org.springframework.stereotype.Component

@Component
class PaymentFacade(
	private val orderService: OrderService,
	private val balanceService: BalanceService,
	private val paymentService: PaymentService
) {
	fun orderPayment(userId: Long, orderId: Long): PaymentResultInfo {
		// 주문 정보 조회
		val orderDto = orderService.getOrderById(orderId)

		// 사용자의 잔액 차감
		val balanceDto = balanceService.updateAmountDecrease(userId, orderDto.totalPrice)

		// 결제 정보 저장
		val registerPayment = paymentService.registerPayment(PaymentDto.of(userId, orderDto))

		// 주문 정보에서 결제 완료로 상태 업데이트
		orderService.updateOrderStatus(orderId, OrderStatus.PAYMENT_COMPLETE)

		// TODO: 외부 데이터 플랫폼에 전송

		return PaymentResultInfo.of(registerPayment, balanceDto, orderId)
	}
}