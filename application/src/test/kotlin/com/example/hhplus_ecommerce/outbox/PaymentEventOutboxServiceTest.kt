package com.example.hhplus_ecommerce.outbox

import com.example.hhplus_ecommerce.outbox.dto.PaymentEventOutboxRequestDto
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

@SpringBootTest
class PaymentEventOutboxServiceTest {
	@Autowired private lateinit var paymentEventOutboxService: PaymentEventOutboxService
	@Autowired private lateinit var paymentEventOutboxRepository: PaymentEventOutboxRepository

	@BeforeEach
	fun clearDB() {
		paymentEventOutboxRepository.deleteAll()
	}

	@Test
	@DisplayName("결제의 Outbox 데이터의 상태를 PUBLISH로 업데이트하는 기능을 검사한다")
	fun updateEventStatusPublish() {
		paymentEventOutboxRepository.insert(
			PaymentEventOutbox(
				paymentEventOutboxId = 0,
				userId = 1L,
				orderId = 123L,
				eventStatus = OutboxEventStatus.INIT,
				createdDate = LocalDateTime.now()
			)
		)

		paymentEventOutboxService.updateEventStatus(
			PaymentEventOutboxRequestDto(
				userId = 1L,
				orderId = 123L,
				eventStatus = OutboxEventStatus.PUBLISH
			)
		)

		val actual = paymentEventOutboxRepository.getByUserIdAndOrderId(
			userId = 1L,
			orderId = 123L
		)

		assertThat(actual.eventStatus).isEqualTo(OutboxEventStatus.PUBLISH)
	}

	@Test
	@DisplayName("결제의 Outbox 데이터의 상태를 COMPLETE로 업데이트하는 기능을 검사한다")
	fun updateEventStatusComplete() {
		paymentEventOutboxRepository.insert(
			PaymentEventOutbox(
				paymentEventOutboxId = 0,
				userId = 1L,
				orderId = 123L,
				eventStatus = OutboxEventStatus.INIT,
				createdDate = LocalDateTime.now()
			)
		)

		paymentEventOutboxService.updateEventStatus(
			PaymentEventOutboxRequestDto(
				userId = 1L,
				orderId = 123L,
				eventStatus = OutboxEventStatus.COMPLETE
			)
		)

		val actual = paymentEventOutboxRepository.getByUserIdAndOrderId(
			userId = 1L,
			orderId = 123L
		)

		assertThat(actual.eventStatus).isEqualTo(OutboxEventStatus.COMPLETE)
	}
}