package com.example.hhplus_ecommerce.domain.product

import com.example.hhplus_ecommerce.application.product.ProductService
import com.example.hhplus_ecommerce.infrastructure.product.entity.Product
import com.example.hhplus_ecommerce.infrastructure.product.entity.ProductDetail
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.anyLong
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.springframework.data.domain.PageRequest

@ExtendWith(MockitoExtension::class)
class ProductServiceTest {
	@Mock
	lateinit var productRepository: ProductRepository

	@Mock
	lateinit var productDetailRepository: ProductDetailRepository

	@InjectMocks
	lateinit var productService: ProductService

	@Test
	@DisplayName("상품 목록 조회 비즈니스 로직 단위 테스트")
	fun getProductListWithPaging() {
		`when`(productRepository.getAllByPaging(any()))
			.thenReturn(
				listOf(
					Product(1L, "상품A", "상품A 설명"),
					Product(2L, "상품B", "상품B 설명"),
				)
			)

		`when`(productDetailRepository.getAllByProductIdsIn(any()))
			.thenReturn(
				listOf(
					ProductDetail(1L, 10000, 100, ProductCategory.CLOTHES),
					ProductDetail(2L, 5000, 30, ProductCategory.COSMETICS),
				)
			)

		val actual = productService.getAllProductInfosWithPaging(PageRequest.of(1, 10))

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

	@Test
	@DisplayName("특정 상품 조회 비즈니스 로직 단위 테스트")
	fun getProductById() {
		doAnswer { invocation ->
			Product(
				1L,
				"상품A",
				"상품A 설명",
			)
		}.`when`(productRepository).getById(anyLong())

		doAnswer { invocation ->
			ProductDetail(
				1L,
				10000,
				100,
				ProductCategory.CLOTHES,
			)
		}.`when`(productDetailRepository).getByProductId(anyLong())

		val actual = productService.getProductInfoById(1L)

		assertThat(actual).isNotNull
		assertThat(actual.productId).isEqualTo(1L)
		assertThat(actual.name).isEqualTo("상품A")
		assertThat(actual.price).isEqualTo(10000)
		assertThat(actual.stockQuantity).isEqualTo(100)
	}
}