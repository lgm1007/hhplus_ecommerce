package com.example.hhplus_ecommerce.payment

import com.example.hhplus_ecommerce.balance.BalanceService
import com.example.hhplus_ecommerce.order.OrderService
import com.example.hhplus_ecommerce.order.OrderStatus
import com.example.hhplus_ecommerce.payment.dto.AfterPaymentEventInfoDto
import com.example.hhplus_ecommerce.payment.dto.PaymentDto
import com.example.hhplus_ecommerce.payment.dto.PaymentEventRequestInfoDto
import com.example.hhplus_ecommerce.payment.dto.PaymentResultInfoDto
import com.example.hhplus_ecommerce.payment.event.AfterPaymentEvent
import com.example.hhplus_ecommerce.payment.event.BeforePaymentEvent
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
	fun orderPayment(userId: Long, orderId: Long): PaymentResultInfoDto {
		// 결제 진행 전 이벤트 발행
		eventPublisher.publishEvent(
			BeforePaymentEvent(
				PaymentEventRequestInfoDto(userId, orderId)
			)
		)

		// 주문 정보 조회
		val orderDto = orderService.getOrderById(orderId)

		// 사용자의 잔액 차감
		val balanceDto = balanceService.updateAmountDecrease(userId, orderDto.totalPrice)

		// 결제 정보 저장
		val registerPayment = paymentService.registerPayment(PaymentDto.of(userId, orderDto))

		// 주문 정보에서 결제 완료로 상태 업데이트
		orderService.updateOrderStatus(orderId, OrderStatus.PAYMENT_COMPLETE)

		// 결제 완료 이벤트 발행
		eventPublisher.publishEvent(
			AfterPaymentEvent(
				paymentEventInfoDto = AfterPaymentEventInfoDto(
					userId = userId,
					orderId = orderId,
					currentBalance = balanceDto.amount,
					paymentDate = registerPayment.createdDate!!
				)
			)
		)

		return PaymentResultInfoDto.of(registerPayment, balanceDto, orderId)
	}
}