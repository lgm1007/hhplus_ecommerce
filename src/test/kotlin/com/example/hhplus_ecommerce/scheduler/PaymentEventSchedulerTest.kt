package com.example.hhplus_ecommerce.scheduler

import com.example.hhplus_ecommerce.application.scheduler.PaymentEventScheduler
import com.example.hhplus_ecommerce.domain.balance.BalanceRepository
import com.example.hhplus_ecommerce.domain.balance.dto.BalanceDto
import com.example.hhplus_ecommerce.domain.outbox.OutboxEventStatus
import com.example.hhplus_ecommerce.domain.outbox.PaymentEventOutboxRepository
import com.example.hhplus_ecommerce.domain.outbox.dto.PaymentEventOutboxDto
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.kafka.test.context.EmbeddedKafka
import java.time.LocalDateTime
import kotlin.test.Test

@SpringBootTest
@EmbeddedKafka(partitions = 3, brokerProperties = ["listeners=PLAINTEXT://localhost:9092"], ports = [9092])
class PaymentEventSchedulerTest {
	@Autowired lateinit var paymentEventScheduler: PaymentEventScheduler
	@Autowired lateinit var paymentEventOutboxRepository: PaymentEventOutboxRepository
	@Autowired lateinit var balanceRepository: BalanceRepository
	@Autowired lateinit var jdbcTemplate: JdbcTemplate

	@BeforeEach
	fun clearDB() {
		paymentEventOutboxRepository.deleteAll()
		balanceRepository.deleteAll()
	}

	@Test
	@DisplayName("INIT 상태이며 저장된 지 5분 경과된 결제 Outbox 이벤트만 재시도하여 COMPLETE 상태로 업데이트 되었는지 확인")
	fun retryPaymentEventInitStatus() {
		paymentEventOutboxRepository.insert(PaymentEventOutboxDto(
			0, 1L, 123L, OutboxEventStatus.INIT, LocalDateTime.now()
		))
		paymentEventOutboxRepository.insert(PaymentEventOutboxDto(
			0, 2L, 456L, OutboxEventStatus.INIT, LocalDateTime.now()
		))

		balanceRepository.insert(BalanceDto(
			0, 1L, 10000, LocalDateTime.now(), LocalDateTime.now()
		))
		balanceRepository.insert(BalanceDto(
			0, 2L, 20000, LocalDateTime.now(), LocalDateTime.now()
		))

		val createDateUpdateSql = "UPDATE PaymentEventOutbox SET createdDate = FORMATDATETIME('2021-01-01 00:00:00', 'yyyy-MM-dd') WHERE userId = ${1L} AND orderId = ${123L}"
		jdbcTemplate.execute(createDateUpdateSql)

		paymentEventScheduler.retryPaymentEventInitStatus()

		Thread.sleep(2000)

		val actual1 = paymentEventOutboxRepository.getByUserIdAndOrderId(1L, 123L)
		val actual2 = paymentEventOutboxRepository.getByUserIdAndOrderId(2L, 456L)

		assertThat(actual1.eventStatus).isEqualTo(OutboxEventStatus.COMPLETE)
		assertThat(actual2.eventStatus).isEqualTo(OutboxEventStatus.INIT)
	}

	@Test
	@DisplayName("PUBLISH 상태이며 저장된 지 5분 경과된 결제 Outbox 이벤트만 재시도하여 COMPLETE 상태로 업데이트 되었는지 확인")
	fun retryPaymentEventPublishStatus() {
		paymentEventOutboxRepository.insert(PaymentEventOutboxDto(
			0, 1L, 123L, OutboxEventStatus.PUBLISH, LocalDateTime.now()
		))
		paymentEventOutboxRepository.insert(PaymentEventOutboxDto(
			0, 2L, 456L, OutboxEventStatus.PUBLISH, LocalDateTime.now()
		))

		balanceRepository.insert(BalanceDto(
			0, 1L, 10000, LocalDateTime.now(), LocalDateTime.now()
		))
		balanceRepository.insert(BalanceDto(
			0, 2L, 20000, LocalDateTime.now(), LocalDateTime.now()
		))

		val createDateUpdateSql = "UPDATE PaymentEventOutbox SET createdDate = FORMATDATETIME('2021-01-01 00:00:00', 'yyyy-MM-dd') WHERE userId = ${1L} AND orderId = ${123L}"
		jdbcTemplate.execute(createDateUpdateSql)

		paymentEventScheduler.retryPaymentEventPublishStatus()

		Thread.sleep(2000)

		val actual1 = paymentEventOutboxRepository.getByUserIdAndOrderId(1L, 123L)
		val actual2 = paymentEventOutboxRepository.getByUserIdAndOrderId(2L, 456L)

		assertThat(actual1.eventStatus).isEqualTo(OutboxEventStatus.COMPLETE)
		assertThat(actual2.eventStatus).isEqualTo(OutboxEventStatus.PUBLISH)
	}
}