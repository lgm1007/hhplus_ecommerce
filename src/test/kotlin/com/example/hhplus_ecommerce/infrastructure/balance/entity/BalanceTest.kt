package com.example.hhplus_ecommerce.infrastructure.balance.entity

import com.example.hhplus_ecommerce.domain.share.exception.ErrorStatus
import com.example.hhplus_ecommerce.domain.share.exception.BadRequestException
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class BalanceTest {
    @Test
    @DisplayName("재고 사용 (차감) 기능 테스트")
    fun decreaseAmount() {
        val balance = BalanceEntity(1L, 10000)

        balance.decreaseAmount(5000)

        assertThat(balance.amount).isEqualTo(5000)
    }

    @Test
    @DisplayName("재고 차감 시 잔액 부족 예외케이스 테스트")
    fun shouldFailWhenNotEnoughAmount() {
        val balance = BalanceEntity(1L, 10000)

        assertThatThrownBy { balance.decreaseAmount(20000) }
            .isInstanceOf(BadRequestException::class.java)
            .extracting("errorStatus")
            .isEqualTo(ErrorStatus.NOT_ENOUGH_BALANCE)
    }

    @Test
    @DisplayName("잔액 충전 기능 테스트")
    fun chargeAmount() {
        val balance = BalanceEntity(1L, 10000)

        balance.chargeAmount(5000)

        assertThat(balance.amount).isEqualTo(15000)
    }

    @Test
    @DisplayName("잔액 충전 시 충전 금액이 음수인 경우 예외케이스 테스트")
    fun shouldFailWhenAmountNegative() {
        val balance = BalanceEntity(1L, 10000)

        assertThatThrownBy { balance.chargeAmount(-1000) }
            .isInstanceOf(BadRequestException::class.java)
            .extracting("errorStatus")
            .isEqualTo(ErrorStatus.CHARGED_AMOUNT_ERROR)
    }
}