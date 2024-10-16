package com.example.hhplus_ecommerce.usecase.order

import com.example.hhplus_ecommerce.domain.order.OrderService
import com.example.hhplus_ecommerce.domain.order.dto.OrderDto
import com.example.hhplus_ecommerce.domain.order.dto.OrderItemDto
import com.example.hhplus_ecommerce.domain.order.dto.OrderItemInfo
import com.example.hhplus_ecommerce.domain.product.ProductCategory
import com.example.hhplus_ecommerce.domain.product.ProductService
import com.example.hhplus_ecommerce.domain.product.dto.ProductDetailDto
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class OrderFacadeUnitTest {
    @Mock
    lateinit var orderService: OrderService

    @Mock
    lateinit var productService: ProductService

    @InjectMocks
    lateinit var orderFacade: OrderFacade

    @Test
    @DisplayName("상품 여러 건에 대해 주문 성공하는 기능 테스트")
    fun orderSuccess2Products() {
        `when`(productService.getAllProductDetailsByDetailIdsInWithLock(any()))
            .thenReturn(
                listOf(
                    ProductDetailDto(1L, 1L, 10000, 50, ProductCategory.CLOTHES, LocalDateTime.now(), LocalDateTime.now()),
                    ProductDetailDto(2L, 2L, 5000, 10, ProductCategory.COSMETICS, LocalDateTime.now(), LocalDateTime.now()),
                )
            )
        doAnswer { invocation -> invocation.arguments[0] as OrderDto }
            .`when`(orderService).registerOrder(any())

        doAnswer { invocation -> invocation.arguments[0] as List<OrderItemDto> }
            .`when`(orderService).registerOrderItems(any())

        val actual = orderFacade.productOrder(
            123L,
            listOf(
                OrderItemInfo(1L, 1),
                OrderItemInfo(2L, 2),
            )
        )

        assertThat(actual).isNotNull
        assertThat(actual.totalPrice).isEqualTo(20000)
    }
}