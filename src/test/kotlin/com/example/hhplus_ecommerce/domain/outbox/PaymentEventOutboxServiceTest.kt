package com.example.hhplus_ecommerce.domain.outbox

import com.example.hhplus_ecommerce.domain.outbox.dto.PaymentEventOutboxDto
import com.example.hhplus_ecommerce.domain.outbox.dto.PaymentEventOutboxRequestDto
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
			PaymentEventOutboxDto(
				0,
				1L,
				123L,
				OutboxEventStatus.INIT,
				LocalDateTime.now()
			)
		)

		paymentEventOutboxService.updateEventStatusPublish(
			PaymentEventOutboxRequestDto(1L, 123L)
		)

		val actual = paymentEventOutboxRepository.getByUserIdAndOrderId(1L, 123L)

		assertThat(actual.eventStatus).isEqualTo(OutboxEventStatus.PUBLISH)
	}

	@Test
	@DisplayName("결제의 Outbox 데이터의 상태를 COMPLETE로 업데이트하는 기능을 검사한다")
	fun updateEventStatusComplete() {
		paymentEventOutboxRepository.insert(
			PaymentEventOutboxDto(
				0,
				1L,
				123L,
				OutboxEventStatus.INIT,
				LocalDateTime.now()
			)
		)

		paymentEventOutboxService.updateEventStatusComplete(
			PaymentEventOutboxRequestDto(1L, 123L)
		)

		val actual = paymentEventOutboxRepository.getByUserIdAndOrderId(1L, 123L)

		assertThat(actual.eventStatus).isEqualTo(OutboxEventStatus.COMPLETE)
	}
}