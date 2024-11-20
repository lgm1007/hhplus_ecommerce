package com.example.hhplus_ecommerce.usecase.payment

import com.example.hhplus_ecommerce.domain.balance.BalanceService
import com.example.hhplus_ecommerce.domain.order.OrderService
import com.example.hhplus_ecommerce.domain.order.OrderStatus
import com.example.hhplus_ecommerce.domain.outbox.OutboxEventStatus
import com.example.hhplus_ecommerce.domain.outbox.PaymentEventOutboxRepository
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
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.orm.ObjectOptimisticLockingFailureException
import java.time.LocalDateTime
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

@SpringBootTest
@EmbeddedKafka(partitions = 3, brokerProperties = ["listeners=PLAINTEXT://localhost:9092"], ports = [9092])
class PaymentFacadeIntegrationTest {
	@Autowired private lateinit var paymentFacade: PaymentFacade
	@Autowired private lateinit var orderService: OrderService
	@Autowired private lateinit var balanceService: BalanceService
	@Autowired private lateinit var balanceRepository: BalanceJpaRepository
	@Autowired private lateinit var balanceHistoryRepository: BalanceHistoryJpaRepository
	@Autowired private lateinit var paymentRepository: PaymentJpaRepository
	@Autowired private lateinit var orderRepository: OrderJpaRepository
	@Autowired private lateinit var paymentEventOutboxRepository: PaymentEventOutboxRepository

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
		val balanceDto = balanceService.getByUserId(userId)

		// 기존 잔액 10000 - 결제 금액 10000 = 남은 잔액 0
		assertThat(balanceDto.amount).isEqualTo(0)
		// 주문 상태가 PAYMENT_COMPLETE(결제 완료)로 업데이트 되었는지 확인
		assertThat(orderDto.orderStatus).isEqualTo(OrderStatus.PAYMENT_COMPLETE)
	}

	@Test
	@DisplayName("결제 요청 - 동시에 결제 요청 시 한 번만 성공시키고 나머지는 실패 처리한다")
	fun orderPaymentConcurrency() {
		// 잔액 50,000이 있고, 주문할 금액이 12,000씩 10번 동시에 요청할 때
		// 같은 주문에 대해 동시에 결제 요청이 들어오는 경우 한 번만 성공시키며 나머지는 실패 처리한다.
		// 예상 성공 카운트 1, 실패 카운트 9, 남은 잔액 38,000
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
					} catch (e: ObjectOptimisticLockingFailureException) {
						failCount.incrementAndGet()
					} finally {
						countDownLatch.countDown()
					}
				}
			}
			countDownLatch.await()

			val balanceDto = balanceService.getByUserId(userId)

			assertThat(balanceDto.amount).isEqualTo(38000)
			assertThat(successCount.get()).isEqualTo(1)
			assertThat(failCount.get()).isEqualTo(9)
		} finally {
			executor.shutdown()
		}
	}

	@Test
	@DisplayName("결제 요청 정상 처리 후 outbox 메시지 상태가 COMPLETE 인지 확인한다")
	fun outboxStatusUpdateCompleteAfterPayment() {
		val userId = 1L
		val orderId = orderRepository.save(OrderTable(userId, LocalDateTime.now(), 10000, OrderStatus.ORDER_COMPLETE)).id
		balanceRepository.save(Balance(userId, 10000))

		paymentFacade.orderPayment(userId, orderId)

		// 메시지 컨슈밍까지 완료 보장시간 2초
		Thread.sleep(2000)

		val actual = paymentEventOutboxRepository.getByUserIdAndOrderId(userId, orderId)

		assertThat(actual.eventStatus).isEqualTo(OutboxEventStatus.COMPLETE)
	}
}