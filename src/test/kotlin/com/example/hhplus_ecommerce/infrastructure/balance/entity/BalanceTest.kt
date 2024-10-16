package com.example.hhplus_ecommerce.infrastructure.balance.entity

import com.example.hhplus_ecommerce.api.error.ErrorStatus
import com.example.hhplus_ecommerce.exception.BadRequestException
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class BalanceTest {
    @Test
    @DisplayName("재고 사용 (차감) 기능 테스트")
    fun decreaseAmount() {
        val balance = Balance(1L, 10000)

        balance.decreaseAmount(5000)

        assertThat(balance.amount).isEqualTo(5000)
    }

    @Test
    @DisplayName("재고 차감 시 잔액 부족 예외케이스 테스트")
    fun shouldFailWhenNotEnoughAmount() {
        val balance = Balance(1L, 10000)

        assertThatThrownBy { balance.decreaseAmount(20000) }
            .isInstanceOf(BadRequestException::class.java)
            .extracting("errorStatus")
            .isEqualTo(ErrorStatus.NOT_ENOUGH_BALANCE)
    }
}