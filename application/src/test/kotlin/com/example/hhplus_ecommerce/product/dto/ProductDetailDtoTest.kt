package com.example.hhplus_ecommerce.product.dto

import com.example.hhplus_ecommerce.product.ProductCategory
import com.example.hhplus_ecommerce.product.ProductDetail
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ProductDetailDtoTest {
	@Test
	@DisplayName("ProductDetail 도메인 모델을 통해 Dto 객체로 생성하는 정적 팩토리 메서드 기능 테스트")
	fun productDetailDtoFromEntity() {
		val productDetail = ProductDetail(
			productId = 1L,
			price = 10000,
			stockQuantity = 100,
			productCategory = ProductCategory.CLOTHES
		)

		val actual = ProductDetailDto.from(productDetail)

		assertThat(actual).isNotNull
		assertThat(actual.productId).isEqualTo(1L)
		assertThat(actual.price).isEqualTo(10000)
		assertThat(actual.stockQuantity).isEqualTo(100)
		assertThat(actual.productCategory).isEqualTo(ProductCategory.CLOTHES)
	}
}