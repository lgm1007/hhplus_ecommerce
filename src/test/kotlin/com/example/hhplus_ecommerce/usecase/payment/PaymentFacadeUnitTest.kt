package com.example.hhplus_ecommerce.usecase.payment

import com.example.hhplus_ecommerce.domain.balance.BalanceService
import com.example.hhplus_ecommerce.domain.balance.dto.BalanceDto
import com.example.hhplus_ecommerce.domain.order.OrderService
import com.example.hhplus_ecommerce.domain.order.OrderStatus
import com.example.hhplus_ecommerce.domain.order.dto.OrderDto
import com.example.hhplus_ecommerce.domain.payment.PaymentService
import com.example.hhplus_ecommerce.domain.payment.PaymentStatus
import com.example.hhplus_ecommerce.domain.payment.dto.PaymentDto
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class PaymentFacadeUnitTest {
    @Mock
    lateinit var orderService: OrderService

    @Mock
    lateinit var balanceService: BalanceService

    @Mock
    lateinit var paymentService: PaymentService

    @Mock
    lateinit var eventPublisher: ApplicationEventPublisher

    @InjectMocks
    lateinit var paymentFacade: PaymentFacade

    @Test
    @DisplayName("상품 결제 기능에 대한 대역 단위테스트")
    fun paymentSuccessByOrder() {
        doAnswer { invocation ->
            OrderDto(
                invocation.arguments[0] as Long,
                123L,
                LocalDateTime.now(),
                10000,
                OrderStatus.ORDER_COMPLETE,
                LocalDateTime.now(),
                LocalDateTime.now(),
            )
        }.`when`(orderService).getOrderById(anyLong())

        doAnswer { invocation ->
            BalanceDto(
                555L,
                invocation.arguments[0] as Long,
                0,
                LocalDateTime.now(),
                LocalDateTime.now(),
            )
        }.`when`(balanceService).updateAmountDecrease(anyLong(), any())

        doAnswer { invocation ->
            val inputPaymentDto = invocation.arguments[0] as PaymentDto
            PaymentDto(
                5678L,
                inputPaymentDto.userId,
                inputPaymentDto.orderId,
                inputPaymentDto.price,
                PaymentStatus.PAYMENT_COMPLETE,
                LocalDateTime.now(),
                LocalDateTime.now()
            )
        }.`when`(paymentService).registerPayment(any())

        val actual = paymentFacade.orderPayment(123L, 1L)

        assertThat(actual).isNotNull
        assertThat(actual.orderId).isEqualTo(1L)
        assertThat(actual.currentBalance).isEqualTo(0)
    }
}