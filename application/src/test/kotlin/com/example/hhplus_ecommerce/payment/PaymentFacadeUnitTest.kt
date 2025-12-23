package com.example.hhplus_ecommerce.payment

import com.example.hhplus_ecommerce.balance.BalanceService
import com.example.hhplus_ecommerce.balance.dto.BalanceDto
import com.example.hhplus_ecommerce.order.OrderService
import com.example.hhplus_ecommerce.order.OrderStatus
import com.example.hhplus_ecommerce.order.dto.OrderDto
import com.example.hhplus_ecommerce.payment.dto.PaymentDto
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
                orderId = invocation.arguments[0] as Long,
                userId = 123L,
                orderDate = LocalDateTime.now(),
                totalPrice = 10000,
                orderStatus = OrderStatus.ORDER_COMPLETE,
                createdDate = LocalDateTime.now(),
                lastModifiedDate = LocalDateTime.now()
            )
        }.`when`(orderService).getOrderById(anyLong())

        doAnswer { invocation ->
            BalanceDto(
                balanceId = 555L,
                userId = invocation.arguments[0] as Long,
                amount = 0,
                createdDate = LocalDateTime.now(),
                lastModifiedDate = LocalDateTime.now(),
            )
        }.`when`(balanceService).updateAmountDecrease(anyLong(), any())

        doAnswer { invocation ->
            val inputPaymentDto = invocation.arguments[0] as PaymentDto
            PaymentDto(
                paymentId = 5678L,
                userId = inputPaymentDto.userId,
                orderId = inputPaymentDto.orderId,
                price = inputPaymentDto.price,
                paymentStatus = PaymentStatus.PAYMENT_COMPLETE,
                createdDate = LocalDateTime.now(),
                lastModifiedDate = LocalDateTime.now()
            )
        }.`when`(paymentService).registerPayment(any())

        val actual = paymentFacade.orderPayment(123L, 1L)

        assertThat(actual).isNotNull
        assertThat(actual.orderId).isEqualTo(1L)
        assertThat(actual.currentBalance).isEqualTo(0)
    }
}