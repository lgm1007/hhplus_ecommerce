package com.example.hhplus_ecommerce.domain.balance

import com.example.hhplus_ecommerce.api.error.ErrorStatus
import com.example.hhplus_ecommerce.exception.BadRequestException
import com.example.hhplus_ecommerce.infrastructure.balance.BalanceHistoryJpaRepository
import com.example.hhplus_ecommerce.infrastructure.balance.BalanceJpaRepository
import com.example.hhplus_ecommerce.infrastructure.balance.entity.Balance
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

@SpringBootTest
class BalanceServiceIntegrationTest {
	@Autowired private lateinit var balanceService: BalanceService
	@Autowired private lateinit var balanceRepository: BalanceJpaRepository
	@Autowired private lateinit var balanceHistoryRepository: BalanceHistoryJpaRepository

	@BeforeEach
	fun clearDB() {
		balanceRepository.deleteAll()
		balanceHistoryRepository.deleteAll()
	}

	@Test
	@DisplayName("잔액 차감 - 한 번 차감 수행")
	fun balanceDecreaseOnce() {
		val balance = Balance(1L, 10000)
		balanceRepository.save(balance)

		balanceService.updateAmountDecrease(1L, 8000)
		val actual = balanceService.getByUserIdWithLock(1L)

		assertThat(actual.amount).isEqualTo(2000)
	}

	@Test
	@DisplayName("잔액 차감 - 잔액이 부족할 경우 예외 케이스")
	fun shouldFailAmountNotEnough() {
		val balance = Balance(1L, 5000)
		balanceRepository.save(balance)

		assertThatThrownBy { balanceService.updateAmountDecrease(1L, 10000) }
			.isInstanceOf(BadRequestException::class.java)
			.extracting("errorStatus")
			.isEqualTo(ErrorStatus.NOT_ENOUGH_BALANCE)
	}

	@Test
	@DisplayName("잔액 차감 - 동시 수행할 시 동시성 제어 테스트")
	fun balanceDecreaseConcurrency() {
		// 10,000 잔액 보유한 사용자에 대해 3000 씩 5번 동시 차감 수행
		// 예상 성공 카운트 3, 실패 카운트 2, 남은 잔액 1000
		val balance = Balance(1L, 10000)
		balanceRepository.save(balance)

		val executor = Executors.newFixedThreadPool(5)
		val countDownLatch = CountDownLatch(5)
		val successCount = AtomicInteger(0) // 성공 카운트
		val failCount = AtomicInteger(0)    // 실패 카운트

		try {
			repeat(5) {
				executor.submit {
					try {
						balanceService.updateAmountDecrease(1L, 3000)
						successCount.incrementAndGet()
					} catch (e: BadRequestException) {
						failCount.incrementAndGet()
					} finally {
						countDownLatch.countDown()
					}
				}
			}

			countDownLatch.await()

			val actual = balanceService.getByUserIdWithLock(1L)

			assertThat(actual.amount).isEqualTo(1000)
			assertThat(successCount.get()).isEqualTo(3)
			assertThat(failCount.get()).isEqualTo(2)
		} finally {
			executor.shutdown()
		}
	}

	@Test
	@DisplayName("잔액 충전 - 한 번 충전 수행")
	fun balanceChargeOnce() {
		val balance = Balance(1L, 5000)
		balanceRepository.save(balance)

		balanceService.updateAmountCharge(1L, 10000)
		val actual = balanceService.getByUserIdWithLock(1L)

		assertThat(actual.amount).isEqualTo(15000)
	}

	@Test
	@DisplayName("잔액 충전 - 충전 금액이 마이너스일 경우 예외 케이스")
	fun shouldFailChargeAmountNegative() {
		val balance = Balance(1L, 5000)
		balanceRepository.save(balance)

		assertThatThrownBy { balanceService.updateAmountCharge(1L, -10000) }
			.isInstanceOf(BadRequestException::class.java)
			.extracting("errorStatus")
			.isEqualTo(ErrorStatus.CHARGED_AMOUNT_ERROR)
	}

	@Test
	@DisplayName("잔액 충전 - 동시 수행할 경우 동시성 제어 테스트")
	fun balanceChargeConcurrency() {
		val balance = Balance(1L, 0)
		balanceRepository.save(balance)

		val executor = Executors.newFixedThreadPool(5)
		val countDownLatch = CountDownLatch(5)

		try {
			repeat(5) {
				executor.submit {
					try {
						balanceService.updateAmountCharge(1L, 10000)
					} finally {
						countDownLatch.countDown()
					}
				}
			}

			countDownLatch.await()

			val actual = balanceService.getByUserIdWithLock(1L)

			assertThat(actual.amount).isEqualTo(50000)
		} finally {
			executor.shutdown()
		}
	}
}