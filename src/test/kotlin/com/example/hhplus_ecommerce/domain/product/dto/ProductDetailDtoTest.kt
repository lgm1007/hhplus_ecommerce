package com.example.hhplus_ecommerce.domain.product.dto

import com.example.hhplus_ecommerce.domain.product.ProductCategory
import com.example.hhplus_ecommerce.infrastructure.product.entity.ProductDetail
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ProductDetailDtoTest {
	@Test
	@DisplayName("ProductDetail 엔티티를 통해 Dto 객체로 생성하는 정적 팩토리 메서드 기능 테스트")
	fun productDetailDtoFromEntity() {
		val productDetail = ProductDetail(
			1L,
			10000,
			100,
			ProductCategory.CLOTHES,
		)

		val actual = ProductDetailDto.from(productDetail)

		assertThat(actual).isNotNull
		assertThat(actual.productId).isEqualTo(1L)
		assertThat(actual.price).isEqualTo(10000)
		assertThat(actual.stockQuantity).isEqualTo(100)
		assertThat(actual.productCategory).isEqualTo(ProductCategory.CLOTHES)
	}
}