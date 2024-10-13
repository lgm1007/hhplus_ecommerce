package com.example.hhplus_ecommerce.domain.product.dto

import com.example.hhplus_ecommerce.infrastructure.product.entity.Product
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ProductDtoTest {
	@Test
	@DisplayName("Product 엔티티를 통해 Dto 객체로 생성하는 정적 팩토리 메서드 기능 테스트")
	fun productDtoFromEntity() {
		val product = Product(
			"상품A",
			10000,
			"상품A 설명",
		)

		val actual = ProductDto.from(product)

		assertThat(actual).isNotNull
		assertThat(actual.name).isEqualTo("상품A")
		assertThat(actual.price).isEqualTo(10000)
		assertThat(actual.description).isEqualTo("상품A 설명")
	}
}