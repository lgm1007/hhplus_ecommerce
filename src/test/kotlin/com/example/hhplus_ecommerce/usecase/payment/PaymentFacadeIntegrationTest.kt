package com.example.hhplus_ecommerce.usecase.payment

import com.example.hhplus_ecommerce.domain.balance.BalanceService
import com.example.hhplus_ecommerce.domain.order.OrderService
import com.example.hhplus_ecommerce.domain.order.OrderStatus
import com.example.hhplus_ecommerce.exception.BadRequestException
import com.example.hhplus_ecommerce.infrastructure.balance.BalanceHistoryJpaRepository
import com.example.hhplus_ecommerce.infrastructure.balance.BalanceJpaRepository
import com.example.hhplus_ecommerce.infrastructure.balance.entity.Balance
import com.example.hhplus_ecommerce.infrastructure.order.OrderJpaRepository
import com.example.hhplus_ecommerce.infrastructure.order.entity.OrderTable
import com.example.hhplus_ecommerce.infrastructure.payment.PaymentJpaRepository
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

@SpringBootTest
class PaymentFacadeIntegrationTest {
	@Autowired private lateinit var paymentFacade: PaymentFacade
	@Autowired private lateinit var orderService: OrderService
	@Autowired private lateinit var balanceService: BalanceService
	@Autowired private lateinit var balanceRepository: BalanceJpaRepository
	@Autowired private lateinit var balanceHistoryRepository: BalanceHistoryJpaRepository
	@Autowired private lateinit var paymentRepository: PaymentJpaRepository
	@Autowired private lateinit var orderRepository: OrderJpaRepository

	@BeforeEach
	fun clearDB() {
		balanceRepository.deleteAll()
		balanceHistoryRepository.deleteAll()
		paymentRepository.deleteAll()
		orderRepository.deleteAll()
	}

	@Test
	@DisplayName("결제 요청 - 외부 데이터 플랫폼 통신 중 예외 발생해도 트랜잭션 적용 영향 없는지 테스트")
	fun orderPaymentOnce() {
		val userId = 1L
		val orderId = orderRepository.save(OrderTable(userId, LocalDateTime.now(), 10000, OrderStatus.ORDER_COMPLETE)).id
		balanceRepository.save(Balance(userId, 10000))

		paymentFacade.orderPayment(userId, orderId)
		val orderDto = orderService.getOrderById(orderId)
		val balanceDto = balanceService.getByUserIdWithLock(userId)

		// 기존 잔액 10000 - 결제 금액 10000 = 남은 잔액 0
		assertThat(balanceDto.amount).isEqualTo(0)
		// 주문 상태가 PAYMENT_COMPLETE(결제 완료)로 업데이트 되었는지 확인
		assertThat(orderDto.orderStatus).isEqualTo(OrderStatus.PAYMENT_COMPLETE)
	}

	@Test
	@DisplayName("결제 요청 - 동시에 결제 요청 시 동시성 제어 테스트")
	fun orderPaymentConcurrency() {
		// 잔액 50000이 있고, 주문할 금액이 12000씩 10번 동시에 요청할 때
		// 예상 성공 카운트 4, 실패 카운트 6, 남은 잔액 2000
		val userId = 1L
		val orderId = orderRepository.save(OrderTable(userId, LocalDateTime.now(), 12000, OrderStatus.ORDER_COMPLETE)).id
		balanceRepository.save(Balance(userId, 50000))

		val executor = Executors.newFixedThreadPool(10)
		val countDownLatch = CountDownLatch(10)
		val successCount = AtomicInteger(0) // 성공 카운트
		val failCount = AtomicInteger(0)    // 실패 카운트

		try {
			repeat(10) {
				executor.submit {
					try {
						paymentFacade.orderPayment(userId, orderId)
						successCount.incrementAndGet()
					} catch (e: BadRequestException) {
						failCount.incrementAndGet()
					} finally {
						countDownLatch.countDown()
					}
				}
			}
			countDownLatch.await()

			val balanceDto = balanceService.getByUserIdWithLock(userId)

			assertThat(balanceDto.amount).isEqualTo(2000)
			assertThat(successCount.get()).isEqualTo(4)
			assertThat(failCount.get()).isEqualTo(6)
		} finally {
			executor.shutdown()
		}
	}
}