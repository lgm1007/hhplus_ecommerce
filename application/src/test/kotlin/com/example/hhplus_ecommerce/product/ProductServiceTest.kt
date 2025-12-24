package com.example.hhplus_ecommerce.product

import com.example.hhplus_ecommerce.product.entity.ProductEntity
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
		`when`(productRepository.getAllByPaging(any(), any()))
			.thenReturn(
				listOf(
					Product(productId = 1L, name = "상품A", description = "상품A 설명"),
					Product(productId = 2L, name = "상품B", description = "상품B 설명"),
				)
			)

		`when`(productDetailRepository.getAllByProductIdsIn(any()))
			.thenReturn(
				listOf(
					ProductDetail(productId = 1L, price = 10000, stockQuantity = 100, productCategory = ProductCategory.CLOTHES),
					ProductDetail(productId = 2L, price = 5000, stockQuantity = 30, productCategory = ProductCategory.COSMETICS),
				)
			)

		val actual = productService.getAllProductInfosWithPaging(page = 1, itemSize = 10)

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
			ProductEntity(
				id = 1L,
				name = "상품A",
				description = "상품A 설명",
			)
		}.`when`(productRepository).getById(anyLong())

		doAnswer { invocation ->
			ProductDetail(
				productId = 1L,
				price = 10000,
				stockQuantity = 100,
				productCategory = ProductCategory.CLOTHES,
			)
		}.`when`(productDetailRepository).getByProductId(anyLong())

		val actual = productService.getProductInfoById(productId = 1L)

		assertThat(actual).isNotNull
		assertThat(actual.productId).isEqualTo(1L)
		assertThat(actual.name).isEqualTo("상품A")
		assertThat(actual.price).isEqualTo(10000)
		assertThat(actual.stockQuantity).isEqualTo(100)
	}
}