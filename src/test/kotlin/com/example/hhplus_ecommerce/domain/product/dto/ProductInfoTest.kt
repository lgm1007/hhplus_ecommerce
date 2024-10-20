package com.example.hhplus_ecommerce.domain.product.dto

import com.example.hhplus_ecommerce.domain.product.ProductCategory
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class ProductInfoTest {
	@Test
	@DisplayName("ProductDto와 ProductDetailDto 객체로 ProductInfo를 생성하는 정적 팩토리 메서드 기능 테스트")
	fun productInfoOf() {
		val productDto = ProductDto(
			1L,
			"상품A",
			"상품A 설명",
			LocalDateTime.now(),
		)

		val productDetailDto = ProductDetailDto(
			1L,
			1L,
			10000,
			100,
			ProductCategory.CLOTHES,
			LocalDateTime.now(),
			LocalDateTime.now(),
		)

		val actual = ProductInfo.of(productDto, productDetailDto)

		assertThat(actual).isNotNull
		assertThat(actual.productId).isEqualTo(1L)
		assertThat(actual.name).isEqualTo("상품A")
		assertThat(actual.price).isEqualTo(10000)
		assertThat(actual.stockQuantity).isEqualTo(100)
	}
	
	@Test
	@DisplayName("ProductDto 목록과 ProductDetailDto 목록으로 ProductInfo 목록을 생성하는 정적 팩토리 메서드 기능 테스트")
	fun productInfoListOf() {
		val productDtos = listOf(
			ProductDto(1L, "상품A", "상품A 설명", LocalDateTime.now()),
			ProductDto(2L, "상품B", "상품B 설명", LocalDateTime.now()),
		)
		val productDetailDtos = listOf(
			ProductDetailDto(1L, 1L, 10000, 100, ProductCategory.CLOTHES, LocalDateTime.now(), LocalDateTime.now()),
			ProductDetailDto(2L, 2L, 5000, 30, ProductCategory.COSMETICS, LocalDateTime.now(), LocalDateTime.now()),
		)

		val actual = ProductInfo.listOf(productDtos, productDetailDtos)

		assertThat(actual.size).isEqualTo(2)
		assertThat(actual[0].productId).isEqualTo(1L)
		assertThat(actual[0].name).isEqualTo("상품A")
		assertThat(actual[0].price).isEqualTo(10000)
		assertThat(actual[0].stockQuantity).isEqualTo(100)
		assertThat(actual[1].productId).isEqualTo(2L)
		assertThat(actual[1].name).isEqualTo("상품B")
		assertThat(actual[1].price).isEqualTo(5000)
		assertThat(actual[1].stockQuantity).isEqualTo(30)
	}
}