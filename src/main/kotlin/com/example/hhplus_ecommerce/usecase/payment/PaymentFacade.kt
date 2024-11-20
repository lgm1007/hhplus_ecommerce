package com.example.hhplus_ecommerce.usecase.payment

import com.example.hhplus_ecommerce.domain.balance.BalanceService
import com.example.hhplus_ecommerce.domain.order.OrderService
import com.example.hhplus_ecommerce.domain.order.OrderStatus
import com.example.hhplus_ecommerce.domain.payment.PaymentService
import com.example.hhplus_ecommerce.domain.payment.dto.AfterPaymentEventInfo
import com.example.hhplus_ecommerce.domain.payment.dto.PaymentDto
import com.example.hhplus_ecommerce.domain.payment.dto.PaymentEventRequestInfo
import com.example.hhplus_ecommerce.domain.payment.dto.PaymentResultInfo
import com.example.hhplus_ecommerce.domain.payment.event.AfterPaymentEvent
import com.example.hhplus_ecommerce.domain.payment.event.BeforePaymentEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class PaymentFacade(
	private val orderService: OrderService,
	private val balanceService: BalanceService,
	private val paymentService: PaymentService,
	private val eventPublisher: ApplicationEventPublisher
) {
	@Transactional
	fun orderPayment(userId: Long, orderId: Long): PaymentResultInfo {
		// 결제 진행 전 이벤트 발행
		eventPublisher.publishEvent(BeforePaymentEvent(
			PaymentEventRequestInfo(userId, orderId)
		))

		// 주문 정보 조회
		val orderDto = orderService.getOrderById(orderId)

		// 사용자의 잔액 차감
		val balanceDto = balanceService.updateAmountDecrease(userId, orderDto.totalPrice)

		// 결제 정보 저장
		val registerPayment = paymentService.registerPayment(PaymentDto.of(userId, orderDto))

		// 주문 정보에서 결제 완료로 상태 업데이트
		orderService.updateOrderStatus(orderId, OrderStatus.PAYMENT_COMPLETE)

		// 결제 완료 이벤트 발행
		eventPublisher.publishEvent(AfterPaymentEvent(
			AfterPaymentEventInfo(
				userId,
				orderId,
				balanceDto.amount,
				registerPayment.createdDate
			)
		))

		return PaymentResultInfo.of(registerPayment, balanceDto, orderId)
	}
}