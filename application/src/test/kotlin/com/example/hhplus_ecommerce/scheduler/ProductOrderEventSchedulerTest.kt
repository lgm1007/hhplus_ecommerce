package com.example.hhplus_ecommerce.scheduler

import com.example.hhplus_ecommerce.outbox.OutboxEventStatus
import com.example.hhplus_ecommerce.outbox.ProductOrderEventOutbox
import com.example.hhplus_ecommerce.outbox.ProductOrderEventOutboxRepository
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
class ProductOrderEventSchedulerTest {
	@Autowired lateinit var productOrderEventScheduler: ProductOrderEventScheduler
	@Autowired lateinit var productOrderEventOutboxRepository: ProductOrderEventOutboxRepository
	@Autowired lateinit var jdbcTemplate: JdbcTemplate

	@BeforeEach
	fun clearDB() {
		productOrderEventOutboxRepository.deleteAll()
	}

	@Test
	@DisplayName("INIT 상태이며 저장된 지 5분 경과된 주문 Outbox 이벤트만 재시도하여 COMPLETE 상태로 업데이트 되었는지 확인")
	fun retryOrderEventInitStatus() {
		productOrderEventOutboxRepository.insert(
			ProductOrderEventOutbox(
				productOrderEventOutboxId = 0,
				userId = 1L,
				productDetailId = 123L,
				orderQuantity = 5,
				eventStatus = OutboxEventStatus.INIT,
				createdDate = LocalDateTime.now()
			)
		)
		productOrderEventOutboxRepository.insert(
			ProductOrderEventOutbox(
				productOrderEventOutboxId = 0,
				userId = 2L,
				productDetailId = 456L,
				orderQuantity = 10,
				eventStatus = OutboxEventStatus.INIT,
				createdDate = LocalDateTime.now()
			)
		)

		val createDateUpdateSql = "UPDATE ProductOrderEventOutbox SET createdDate = FORMATDATETIME('2021-01-01 00:00:00', 'yyyy-MM-dd') WHERE userId = ${1L} AND productDetailId = ${123L} AND orderQuantity = ${5}"
		jdbcTemplate.execute(createDateUpdateSql)

		productOrderEventScheduler.retryOrderEventInitStatus()

		Thread.sleep(2000)

		val actual1 = productOrderEventOutboxRepository.getAllByUserIdAndProductDetailId(userId = 1L, productDetailId = 123L)
			.find { it.orderQuantity == 5 }
		val actual2 = productOrderEventOutboxRepository.getAllByUserIdAndProductDetailId(userId = 2L, productDetailId = 456L)
			.find { it.orderQuantity == 10 }

		assertThat(actual1?.eventStatus).isEqualTo(OutboxEventStatus.COMPLETE)
		assertThat(actual2?.eventStatus).isEqualTo(OutboxEventStatus.INIT)
	}

	@Test
	@DisplayName("PUBLISH 상태이며 저장된 지 5분 경과된 주문 Outbox 이벤트만 재시도하여 COMPLETE 상태로 업데이트 되었는지 확인")
	fun retryOrderEventPublishStatus() {
		productOrderEventOutboxRepository.insert(
			ProductOrderEventOutbox(
				productOrderEventOutboxId = 0,
				userId = 1L,
				productDetailId = 123L,
				orderQuantity = 5,
				eventStatus = OutboxEventStatus.PUBLISH,
				createdDate = LocalDateTime.now()
			)
		)
		productOrderEventOutboxRepository.insert(
			ProductOrderEventOutbox(
				productOrderEventOutboxId = 0,
				userId = 2L,
				productDetailId = 456L,
				orderQuantity = 10,
				eventStatus = OutboxEventStatus.PUBLISH,
				createdDate = LocalDateTime.now()
			)
		)

		val createDateUpdateSql = "UPDATE ProductOrderEventOutbox SET createdDate = FORMATDATETIME('2021-01-01 00:00:00', 'yyyy-MM-dd') WHERE userId = ${1L} AND productDetailId = ${123L} AND orderQuantity = ${5}"
		jdbcTemplate.execute(createDateUpdateSql)

		productOrderEventScheduler.retryOrderEventPublishStatus()

		Thread.sleep(2000)

		val actual1 = productOrderEventOutboxRepository.getAllByUserIdAndProductDetailId(userId = 1L, productDetailId = 123L)
			.find { it.orderQuantity == 5 }
		val actual2 = productOrderEventOutboxRepository.getAllByUserIdAndProductDetailId(userId = 2L, productDetailId = 456L)
			.find { it.orderQuantity == 10 }

		assertThat(actual1?.eventStatus).isEqualTo(OutboxEventStatus.COMPLETE)
		assertThat(actual2?.eventStatus).isEqualTo(OutboxEventStatus.PUBLISH)
	}
}