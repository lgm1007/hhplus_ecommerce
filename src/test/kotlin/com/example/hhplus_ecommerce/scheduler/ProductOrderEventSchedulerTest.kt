package com.example.hhplus_ecommerce.scheduler

import com.example.hhplus_ecommerce.application.scheduler.ProductOrderEventScheduler
import com.example.hhplus_ecommerce.domain.outbox.OutboxEventStatus
import com.example.hhplus_ecommerce.domain.outbox.ProductOrderEventOutboxRepository
import com.example.hhplus_ecommerce.domain.outbox.dto.ProductOrderEventOutboxDto
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
			ProductOrderEventOutboxDto(0, 1L, 123L, 5, OutboxEventStatus.INIT, LocalDateTime.now())
		)
		productOrderEventOutboxRepository.insert(
			ProductOrderEventOutboxDto(0, 2L, 456L, 10, OutboxEventStatus.INIT, LocalDateTime.now())
		)

		val createDateUpdateSql = "UPDATE ProductOrderEventOutbox SET createdDate = FORMATDATETIME('2021-01-01 00:00:00', 'yyyy-MM-dd') WHERE userId = ${1L} AND productDetailId = ${123L} AND orderQuantity = ${5}"
		jdbcTemplate.execute(createDateUpdateSql)

		productOrderEventScheduler.retryOrderEventInitStatus()

		Thread.sleep(2000)

		val actual1 = productOrderEventOutboxRepository.getAllByUserIdAndProductDetailId(1L, 123L)
			.find { it.orderQuantity == 5 }
		val actual2 = productOrderEventOutboxRepository.getAllByUserIdAndProductDetailId(2L, 456L)
			.find { it.orderQuantity == 10 }

		assertThat(actual1?.eventStatus).isEqualTo(OutboxEventStatus.COMPLETE)
		assertThat(actual2?.eventStatus).isEqualTo(OutboxEventStatus.INIT)
	}

	@Test
	@DisplayName("PUBLISH 상태이며 저장된 지 5분 경과된 주문 Outbox 이벤트만 재시도하여 COMPLETE 상태로 업데이트 되었는지 확인")
	fun retryOrderEventPublishStatus() {
		productOrderEventOutboxRepository.insert(
			ProductOrderEventOutboxDto(0, 1L, 123L, 5, OutboxEventStatus.PUBLISH, LocalDateTime.now())
		)
		productOrderEventOutboxRepository.insert(
			ProductOrderEventOutboxDto(0, 2L, 456L, 10, OutboxEventStatus.PUBLISH, LocalDateTime.now())
		)

		val createDateUpdateSql = "UPDATE ProductOrderEventOutbox SET createdDate = FORMATDATETIME('2021-01-01 00:00:00', 'yyyy-MM-dd') WHERE userId = ${1L} AND productDetailId = ${123L} AND orderQuantity = ${5}"
		jdbcTemplate.execute(createDateUpdateSql)

		productOrderEventScheduler.retryOrderEventPublishStatus()

		Thread.sleep(2000)

		val actual1 = productOrderEventOutboxRepository.getAllByUserIdAndProductDetailId(1L, 123L)
			.find { it.orderQuantity == 5 }
		val actual2 = productOrderEventOutboxRepository.getAllByUserIdAndProductDetailId(2L, 456L)
			.find { it.orderQuantity == 10 }

		assertThat(actual1?.eventStatus).isEqualTo(OutboxEventStatus.COMPLETE)
		assertThat(actual2?.eventStatus).isEqualTo(OutboxEventStatus.PUBLISH)
	}
}