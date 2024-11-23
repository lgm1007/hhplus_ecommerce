package com.example.hhplus_ecommerce.domain.outbox

import com.example.hhplus_ecommerce.domain.outbox.dto.ProductOrderEventOutboxDto
import com.example.hhplus_ecommerce.domain.outbox.dto.ProductOrderEventOutboxRequestDto
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

@SpringBootTest
class ProductOrderEventOutboxServiceTest {
	@Autowired private lateinit var productOrderEventOutboxService:ProductOrderEventOutboxService
	@Autowired private lateinit var productOrderEventOutboxRepository: ProductOrderEventOutboxRepository

	@BeforeEach
	fun clearDB() {
		productOrderEventOutboxRepository.deleteAll()
	}

	@Test
	@DisplayName("상품 주문의 Outbox 데이터의 상태를 PUBLISH로 업데이트하는 기능을 검사한다")
	fun updateEventStatusPublish() {
		productOrderEventOutboxRepository.insert(
			ProductOrderEventOutboxDto(
				0,
				1L,
				123L,
				100,
				OutboxEventStatus.INIT,
				LocalDateTime.now()
			)
		)

		productOrderEventOutboxService.updateEventStatusPublish(
			ProductOrderEventOutboxRequestDto(
				1L, 123L, 100
			)
		)

		val actual = productOrderEventOutboxRepository.getAllByUserIdAndProductDetailId(1L, 123L)

		actual.forEach {
			assertThat(it.eventStatus).isEqualTo(OutboxEventStatus.PUBLISH)
		}
	}

	@Test
	@DisplayName("상품 주문의 Outbox 데이터의 상태를 COMPLETE로 업데이트하는 기능을 검사한다")
	fun updateEventStatusComplete() {
		productOrderEventOutboxRepository.insert(
			ProductOrderEventOutboxDto(
				0,
				1L,
				123L,
				100,
				OutboxEventStatus.INIT,
				LocalDateTime.now()
			)
		)

		productOrderEventOutboxService.updateEventStatusComplete(
			ProductOrderEventOutboxRequestDto(
				1L, 123L, 100
			)
		)

		val actual = productOrderEventOutboxRepository.getAllByUserIdAndProductDetailId(1L, 123L)

		actual.forEach {
			assertThat(it.eventStatus).isEqualTo(OutboxEventStatus.COMPLETE)
		}
	}
}