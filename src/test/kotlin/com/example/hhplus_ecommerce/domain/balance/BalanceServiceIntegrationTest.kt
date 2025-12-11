package com.example.hhplus_ecommerce.domain.balance

import com.example.hhplus_ecommerce.application.balance.BalanceService
import com.example.hhplus_ecommerce.domain.share.exception.ErrorStatus
import com.example.hhplus_ecommerce.domain.share.exception.BadRequestException
import com.example.hhplus_ecommerce.infrastructure.balance.BalanceHistoryJpaRepository
import com.example.hhplus_ecommerce.infrastructure.balance.BalanceJpaRepository
import com.example.hhplus_ecommerce.infrastructure.balance.entity.BalanceEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.orm.ObjectOptimisticLockingFailureException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

@SpringBootTest
class BalanceServiceIntegrationTest {
	@Autowired private lateinit var balanceService: BalanceService
	@Autowired private lateinit var balanceRepository: BalanceJpaRepository
	@Autowired private lateinit var balanceHistoryRepository: BalanceHistoryJpaRepository
	private val logger = KotlinLogging.logger {}

	@BeforeEach
	fun clearDB() {
		balanceRepository.deleteAll()
		balanceHistoryRepository.deleteAll()
	}

	@Test
	@DisplayName("300000개의 데이터에서 사용자 ID가 298765에 해당하는 잔액 정보 조회")
	fun getBalanceByUserId() {
		givenBalanceDumpData(300000)

		val startTime = System.currentTimeMillis()

		val actual = balanceService.getByUserId(298765L)

		val endTime = System.currentTimeMillis()
		logger.info("실행 시간: ${endTime - startTime} milliseconds")

		assertThat(actual).isNotNull
	}

	private fun givenBalanceDumpData(size: Int) {
		for (i in 1..size) {
			balanceRepository.save(BalanceEntity(i.toLong(), 100))
		}
	}

	@Test
	@DisplayName("잔액 차감 - 한 번 차감 수행")
	fun balanceDecreaseOnce() {
		val balance = BalanceEntity(1L, 10000)
		balanceRepository.save(balance)

		balanceService.updateAmountDecrease(1L, 8000)
		val actual = balanceService.getByUserId(1L)

		assertThat(actual.amount).isEqualTo(2000)
	}

	@Test
	@DisplayName("잔액 차감 - 잔액이 부족할 경우 예외 케이스")
	fun shouldFailAmountNotEnough() {
		val balance = BalanceEntity(1L, 5000)
		balanceRepository.save(balance)

		assertThatThrownBy { balanceService.updateAmountDecrease(1L, 10000) }
			.isInstanceOf(BadRequestException::class.java)
			.extracting("errorStatus")
			.isEqualTo(ErrorStatus.NOT_ENOUGH_BALANCE)
	}

	@Test
	@DisplayName("잔액 차감 - 동시에 잔액 차감 요청이 들어오면 한 번만 성공시키고 나머지 요청은 실패해야 한다")
	fun balanceDecreaseConcurrency() {
		// 10,000 잔액 보유한 사용자에 대해 3,000 씩 5번 동시 차감 수행
		// 동시에 들어온 잔액 차감 요청은 한 번만 성공시키고 나머지 요청은 실패 처리한다.
		// 예상 성공 카운트 1, 실패 카운트 4, 남은 잔액 7,000
		val balance = BalanceEntity(1L, 10000)
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
					} catch (e: ObjectOptimisticLockingFailureException) {
						failCount.incrementAndGet()
					} finally {
						countDownLatch.countDown()
					}
				}
			}

			countDownLatch.await()

			val actual = balanceService.getByUserId(1L)

			assertThat(actual.amount).isEqualTo(7000)
			assertThat(successCount.get()).isEqualTo(1)
			assertThat(failCount.get()).isEqualTo(4)
		} finally {
			executor.shutdown()
		}
	}

	@Test
	@DisplayName("잔액 차감 - Coroutine 도입 동시에 잔액 차감 요청이 들어오면 한 번만 성공시키고 나머지 요청 실패 동시성 제어 테스트")
	fun balanceDecreaseConcurrencyWithCoroutine() {
		// 10,000 잔액 보유한 사용자에 대해 3,000 씩 5번 동시 차감 수행
		// 동시에 들어온 잔액 차감 요청은 한 번만 성공시키고 나머지 요청은 실패 처리한다.
		// 예상 성공 카운트 1, 실패 카운트 4, 남은 잔액 7,000
		val balance = BalanceEntity(1L, 10000)
		balanceRepository.save(balance)

		val successCount = AtomicInteger(0) // 성공 카운트
		val failCount = AtomicInteger(0)    // 실패 카운트

		runBlocking {
			repeat(5) {
				// launch 로 잔액 차감 요청을 개별 Coroutine 에서 실행
				launch(Dispatchers.Default) {   // Dispatchers.Default = 멀티 스레드에서 실행되도록
					try {
						balanceService.updateAmountDecrease(1L, 3000)
						successCount.incrementAndGet()
					} catch (e: ObjectOptimisticLockingFailureException) {
						failCount.incrementAndGet()
					}
				}
			}
		}

		val actual = balanceService.getByUserId(1L)

		assertThat(actual.amount).isEqualTo(7000)
		assertThat(successCount.get()).isEqualTo(1)
		assertThat(failCount.get()).isEqualTo(4)
	}

	@Test
	@DisplayName("잔액 충전 - 한 번 충전 수행")
	fun balanceChargeOnce() {
		val balance = BalanceEntity(1L, 5000)
		balanceRepository.save(balance)

		balanceService.updateAmountCharge(1L, 10000)
		val actual = balanceService.getByUserId(1L)

		assertThat(actual.amount).isEqualTo(15000)
	}

	@Test
	@DisplayName("잔액 충전 - 충전 금액이 마이너스일 경우 예외 케이스")
	fun shouldFailChargeAmountNegative() {
		val balance = BalanceEntity(1L, 5000)
		balanceRepository.save(balance)

		assertThatThrownBy { balanceService.updateAmountCharge(1L, -10000) }
			.isInstanceOf(BadRequestException::class.java)
			.extracting("errorStatus")
			.isEqualTo(ErrorStatus.CHARGED_AMOUNT_ERROR)
	}

	@Test
	@DisplayName("잔액 충전 - 동시에 잔액 충전 요청이 들어오는 경우 한 번만 성공시키고 나머지는 실패 처리한다")
	fun balanceChargeConcurrency() {
		// 동시에 10,000 잔액 충전 요청이 5번 들어오는 경우
		// 동시에 들어온 충전 요청은 한 번만 성공시키고 나머지 요청은 실패 처리한다.
		// 예상 충전 잔액: 10,000
		val balance = BalanceEntity(1L, 0)
		balanceRepository.save(balance)

		val executor = Executors.newFixedThreadPool(5)
		val countDownLatch = CountDownLatch(5)
		val successCount = AtomicInteger(0) // 성공 카운트
		val failCount = AtomicInteger(0)    // 실패 카운트

		try {
			repeat(5) {
				executor.submit {
					try {
						balanceService.updateAmountCharge(1L, 10000)
						successCount.incrementAndGet()
					} catch (e: ObjectOptimisticLockingFailureException) {
						failCount.incrementAndGet()
					} finally {
						countDownLatch.countDown()
					}
				}
			}

			countDownLatch.await()

			val actual = balanceService.getByUserId(1L)

			assertThat(actual.amount).isEqualTo(10000)
			assertThat(successCount.get()).isEqualTo(1)
			assertThat(failCount.get()).isEqualTo(4)
		} finally {
			executor.shutdown()
		}
	}
}