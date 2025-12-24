package com.example.hhplus_ecommerce.product.dto

import com.example.hhplus_ecommerce.product.ProductCategory
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class ProductInfoDtoTest {
	@Test
	@DisplayName("ProductDto와 ProductDetailDto 객체로 ProductInfo를 생성하는 정적 팩토리 메서드 기능 테스트")
	fun productInfoOf() {
		val productDto = ProductDto(
			productId = 1L,
			name = "상품A",
			description = "상품A 설명",
			createdDate = LocalDateTime.now()
		)

		val productDetailDto = ProductDetailDto(
			productDetailId = 1L,
			productId = 1L,
			price = 10000,
			stockQuantity = 100,
			productCategory = ProductCategory.CLOTHES,
			createdDate = LocalDateTime.now(),
			lastModifiedDate = LocalDateTime.now()
		)

		val actual = ProductInfoDto.of(productDto, productDetailDto)

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
			ProductDto(
				productId = 1L,
				name = "상품A",
				description = "상품A 설명",
				createdDate = LocalDateTime.now()
			),
			ProductDto(
				productId = 2L,
				name = "상품B",
				description = "상품B 설명",
				createdDate = LocalDateTime.now()
			),
		)
		val productDetailDtos = listOf(
			ProductDetailDto(
				productDetailId = 1L,
				productId = 1L,
				price = 10000,
				stockQuantity = 100,
				productCategory = ProductCategory.CLOTHES,
				createdDate = LocalDateTime.now(),
				lastModifiedDate = LocalDateTime.now()
			),
			ProductDetailDto(
				productDetailId = 2L,
				productId = 2L,
				price = 5000,
				stockQuantity = 30,
				productCategory = ProductCategory.COSMETICS,
				createdDate = LocalDateTime.now(),
				lastModifiedDate = LocalDateTime.now()
			)
		)

		val actual = ProductInfoDto.listOf(productDtos, productDetailDtos)

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