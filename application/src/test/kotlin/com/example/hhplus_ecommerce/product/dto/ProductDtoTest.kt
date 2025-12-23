package com.example.hhplus_ecommerce.product.dto

import com.example.hhplus_ecommerce.product.Product
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ProductDtoTest {
	@Test
	@DisplayName("Product 도메인 모델을 통해 Dto 객체로 생성하는 정적 팩토리 메서드 기능 테스트")
	fun productDtoFromEntity() {
		val product = Product(
			name = "상품A",
			description = "상품A 설명",
		)

		val actual = ProductDto.from(product)

		assertThat(actual).isNotNull
		assertThat(actual.name).isEqualTo("상품A")
		assertThat(actual.description).isEqualTo("상품A 설명")
	}
}