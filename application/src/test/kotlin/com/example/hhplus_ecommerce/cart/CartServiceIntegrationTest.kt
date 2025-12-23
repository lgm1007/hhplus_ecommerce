package com.example.hhplus_ecommerce.cart

import com.example.hhplus_ecommerce.cart.dto.CartDto
import com.example.hhplus_ecommerce.cart.entity.CartEntity
import com.example.hhplus_ecommerce.share.exception.ErrorStatus
import com.example.hhplus_ecommerce.share.exception.NotFoundException
import mu.KotlinLogging
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

@SpringBootTest
class CartServiceIntegrationTest {
	@Autowired private lateinit var cartService: CartService
	@Autowired private lateinit var cartRepository: CartJpaRepository
	private val logger = KotlinLogging.logger {}

	@BeforeEach
	fun clearDB() {
		cartRepository.deleteAll()
	}

	@Test
	@DisplayName("300000개의 데이터에서 사용자 ID가 298765인 데이터 조회")
	fun getCartByUserId() {
		givenCartDumpData(300000)

		val startTime = System.currentTimeMillis()

		val actual = cartService.getAllCartsByUser(298765L)

		val endTime = System.currentTimeMillis()
		logger.info("실행 시간: ${endTime - startTime} milliseconds")

		assertThat(actual).isNotNull
	}

	private fun givenCartDumpData(size: Int) {
		for (i in 1..size) {
			cartRepository.save(CartEntity(i.toLong(), i.toLong(), 1))
		}
	}

	@Test
	@DisplayName("장바구니 상품 추가 기능 테스트")
	fun addProductCart() {
		// 상품 2개 장바구니에 추가
		val userId = 1L
		cartService.addProductCart(
			CartDto(
				cartId = 0,
				userId = userId,
				productDetailId = 1L,
				quantity = 1,
				createdDate = LocalDateTime.now()
			)
		)
		cartService.addProductCart(
			CartDto(
				cartId = 0,
				userId = userId,
				productDetailId = 5L,
				quantity = 2,
				createdDate = LocalDateTime.now()
			)
		)

		val actual = cartService.getAllCartsByUser(userId)

		assertThat(actual.size).isEqualTo(2)
		assertThat(actual[0].productDetailId).isEqualTo(1L)
		assertThat(actual[0].quantity).isEqualTo(1)
		assertThat(actual[1].productDetailId).isEqualTo(5L)
		assertThat(actual[1].quantity).isEqualTo(2)
	}

	@Test
	@DisplayName("장바구니 상품 삭제 기능 테스트")
	fun deleteCartByUserProduct() {
		// 상품 2개 추가한 후 상품 1개 삭제하기
		val userId = 1L
		cartService.addProductCart(
			CartDto(
				cartId = 0,
				userId = userId,
				productDetailId = 1L,
				quantity = 1,
				createdDate = LocalDateTime.now()
			)
		)
		cartService.addProductCart(
			CartDto(
				cartId = 0,
				userId = userId,
				productDetailId = 5L,
				quantity = 2,
				createdDate = LocalDateTime.now()
			)
		)

		cartService.deleteCartByUserProduct(userId, productDetailId = 1L)

		val actual = cartService.getAllCartsByUser(userId)

		assertThat(actual.size).isEqualTo(1)
		assertThat(actual[0].productDetailId).isEqualTo(5L)
		assertThat(actual[0].quantity).isEqualTo(2)
	}

	@Test
	@DisplayName("해당 사용자에 대한 장바구니 모두 삭제")
	fun deleteAllCartByUser() {
		// 상품 2개 추가한 후 상품 모두 삭제하기
		val userId = 1L
		cartService.addProductCart(
			CartDto(
				cartId = 0,
				userId = userId,
				productDetailId = 1L,
				quantity = 1,
				createdDate = LocalDateTime.now()
			)
		)
		cartService.addProductCart(
			CartDto(
				cartId = 0,
				userId = userId,
				productDetailId = 5L,
				quantity = 2,
				createdDate = LocalDateTime.now()
			)
		)

		cartService.deleteCartByUser(userId)

		assertThatThrownBy { cartService.getAllCartsByUser(userId) }
			.isInstanceOf(NotFoundException::class.java)
			.extracting("errorStatus")
			.isEqualTo(ErrorStatus.NOT_FOUND_CART)
	}
}