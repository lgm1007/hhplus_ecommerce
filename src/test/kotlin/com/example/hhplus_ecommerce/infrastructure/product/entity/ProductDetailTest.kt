package com.example.hhplus_ecommerce.infrastructure.product.entity

import com.example.hhplus_ecommerce.api.error.ErrorStatus
import com.example.hhplus_ecommerce.domain.product.ProductCategory
import com.example.hhplus_ecommerce.exception.BadRequestException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ProductDetailTest {
    @Test
    @DisplayName("재고 차감 기능 테스트")
    fun decreaseQuantity() {
        val productDetail = ProductDetail(
            1L,
            10000,
            10,
            ProductCategory.CLOTHES
        )

        productDetail.decreaseQuantity(5)

        assertThat(productDetail.stockQuantity).isEqualTo(5)
    }

    @Test
    @DisplayName("재고가 없는 경우에 대한 예외케이스 기능 테스트")
    fun shouldFailWhenNotEnoughQuantity() {
        val productDetail = ProductDetail(
            1L,
            10000,
            10,
            ProductCategory.CLOTHES
        )

        assertThatThrownBy { productDetail.decreaseQuantity(20) }
            .isInstanceOf(BadRequestException::class.java)
            .extracting("errorStatus")
            .isEqualTo(ErrorStatus.NOT_ENOUGH_QUANTITY)
    }
}