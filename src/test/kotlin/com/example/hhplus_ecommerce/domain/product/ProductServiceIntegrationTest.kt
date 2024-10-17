package com.example.hhplus_ecommerce.domain.product

import com.example.hhplus_ecommerce.exception.BadRequestException
import com.example.hhplus_ecommerce.infrastructure.product.ProductDetailJpaRepository
import com.example.hhplus_ecommerce.infrastructure.product.ProductJpaRepository
import com.example.hhplus_ecommerce.infrastructure.product.entity.Product
import com.example.hhplus_ecommerce.infrastructure.product.entity.ProductDetail
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

@SpringBootTest
class ProductServiceIntegrationTest {
	@Autowired private lateinit var productService: ProductService
	@Autowired private lateinit var productJpaRepository: ProductJpaRepository
	@Autowired private lateinit var productDetailJpaRepository: ProductDetailJpaRepository

	@Test
	@DisplayName("상품 목록 조회 - 페이징하여 상품 목록 조회")
	fun getAllProductsWithPaging() {
		givenProducts(5)

		val actual = productService.getAllProductInfosWithPaging(PageRequest.of(0, 3))

		assertThat(actual.size).isEqualTo(3)
	}

	private fun givenProducts(size: Int) {
		for (i in 1..size) {
			val productId = productJpaRepository.save(Product("상품${i}", "{i}번 상품")).id
			productDetailJpaRepository.save(ProductDetail(productId, 1000, 100, ProductCategory.CLOTHES))
		}
	}

	@Test
	@DisplayName("특정 상품 조회하기")
	fun getProductInfo() {
		val productId = productJpaRepository.save(Product("상품 A", "A 상품")).id
		productDetailJpaRepository.save(ProductDetail(productId, 1000, 100, ProductCategory.CLOTHES))

		val actual = productService.getProductInfoById(productId)

		assertThat(actual.price).isEqualTo(1000)
		assertThat(actual.stockQuantity).isEqualTo(100)
		assertThat(actual.name).isEqualTo("상품 A")
	}

	@Test
	@DisplayName("상품 재고 차감 - 동시에 재고 차감 시 동시성 제어 테스트")
	fun quantityDecreaseConcurrency() {
		// 상품 재고 3개에 대해 5번 동시 차감 요청 시
		// 예상 성공 카운트 3, 실패 카운트 2, 남은 재고양 0
		val productId = productJpaRepository.save(Product("상품 A", "A 상품")).id
		val detailId = productDetailJpaRepository.save(ProductDetail(productId, 1000, 3, ProductCategory.CLOTHES)).id

		val executor = Executors.newFixedThreadPool(5)
		val countDownLatch = CountDownLatch(5)
		val successCount = AtomicInteger(0) // 성공 카운트
		val failCount = AtomicInteger(0)    // 실패 카운트

		try {
			repeat(5) {
				executor.submit {
					try {
						productService.updateProductQuantityDecrease(detailId, 1)
						successCount.incrementAndGet()
					} catch (e: BadRequestException) {
						failCount.incrementAndGet()
					} finally {
						countDownLatch.countDown()
					}
				}
			}

			countDownLatch.await()

			val actual = productService.getProductInfoById(productId)

			assertThat(actual.stockQuantity).isEqualTo(0)
			assertThat(successCount.get()).isEqualTo(3)
			assertThat(failCount.get()).isEqualTo(2)
		} finally {
			executor.shutdown()
		}
	}
}