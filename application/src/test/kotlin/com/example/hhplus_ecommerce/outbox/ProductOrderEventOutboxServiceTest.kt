package com.example.hhplus_ecommerce.outbox

import com.example.hhplus_ecommerce.outbox.dto.ProductOrderEventOutboxRequestDto
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
			ProductOrderEventOutbox(
				productOrderEventOutboxId = 0,
				userId = 1L,
				productDetailId = 123L,
				orderQuantity = 100,
				eventStatus = OutboxEventStatus.INIT,
				createdDate = LocalDateTime.now()
			)
		)

		productOrderEventOutboxService.updateEventStatus(
			ProductOrderEventOutboxRequestDto(
				userId = 1L,
				productDetailId = 123L,
				orderQuantity = 100,
				eventStatus = OutboxEventStatus.PUBLISH
			)
		)

		val actual = productOrderEventOutboxRepository.getAllByUserIdAndProductDetailId(
			userId = 1L,
			productDetailId = 123L
		)

		actual.forEach {
			assertThat(it.eventStatus).isEqualTo(OutboxEventStatus.PUBLISH)
		}
	}

	@Test
	@DisplayName("상품 주문의 Outbox 데이터의 상태를 COMPLETE로 업데이트하는 기능을 검사한다")
	fun updateEventStatusComplete() {
		productOrderEventOutboxRepository.insert(
			ProductOrderEventOutbox(
				productOrderEventOutboxId = 0,
				userId = 1L,
				productDetailId = 123L,
				orderQuantity = 100,
				eventStatus = OutboxEventStatus.INIT,
				createdDate = LocalDateTime.now()
			)
		)

		productOrderEventOutboxService.updateEventStatus(
			ProductOrderEventOutboxRequestDto(
				userId = 1L,
				productDetailId = 123L,
				orderQuantity = 100,
				eventStatus = OutboxEventStatus.COMPLETE
			)
		)

		val actual = productOrderEventOutboxRepository.getAllByUserIdAndProductDetailId(1L, 123L)

		actual.forEach {
			assertThat(it.eventStatus).isEqualTo(OutboxEventStatus.COMPLETE)
		}
	}
}