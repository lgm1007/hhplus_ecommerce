package com.example.hhplus_ecommerce.order

import com.example.hhplus_ecommerce.cart.CartService
import com.example.hhplus_ecommerce.order.dto.OrderDto
import com.example.hhplus_ecommerce.order.dto.OrderItemDto
import com.example.hhplus_ecommerce.order.dto.OrderItemInfoDto
import com.example.hhplus_ecommerce.product.ProductCategory
import com.example.hhplus_ecommerce.product.ProductService
import com.example.hhplus_ecommerce.product.dto.ProductDetailDto
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
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class OrderFacadeUnitTest {
    @Mock
    lateinit var orderService: OrderService

    @Mock
    lateinit var productService: ProductService

    @Mock
    lateinit var cartService: CartService

    @InjectMocks
    lateinit var orderFacade: OrderFacade

    @Test
    @DisplayName("상품 두 건에 대해 주문하는 대역 단위테스트")
    fun orderSuccess2Products() {
        `when`(productService.getAllProductDetailsByDetailIdsInWithLock(any()))
            .thenReturn(
                listOf(
                    ProductDetailDto(
                        productDetailId = 1L,
                        productId = 1L,
                        price = 10000,
                        stockQuantity = 50,
                        productCategory = ProductCategory.CLOTHES,
                        createdDate = LocalDateTime.now(),
                        lastModifiedDate = LocalDateTime.now()
                    ),
                    ProductDetailDto(
                        productDetailId = 2L,
                        productId = 2L,
                        price = 5000,
                        stockQuantity = 10,
                        productCategory = ProductCategory.COSMETICS,
                        createdDate = LocalDateTime.now(),
                        lastModifiedDate = LocalDateTime.now()
                    )
                )
            )
        `when`(orderService.doOrder(anyLong(), any()))
            .thenReturn(
                Pair(
                    OrderDto(
                        orderId = 1L,
                        userId = 123L,
                        orderDate = LocalDateTime.now(),
                        totalPrice = 20000,
                        orderStatus = OrderStatus.ORDER_COMPLETE,
                        createdDate = LocalDateTime.now(),
                        lastModifiedDate = LocalDateTime.now()
                    ),
                    listOf(
                        OrderItemDto(
                            orderItemId = 1L,
                            orderId = 1L,
                            productDetailId = 1L,
                            quantity = 1,
                            price = 10000,
                            createdDate = LocalDateTime.now()
                        ),
                        OrderItemDto(
                            orderItemId = 2L,
                            orderId = 1L,
                            productDetailId = 2L,
                            quantity = 2,
                            price = 5000,
                            createdDate = LocalDateTime.now()
                        )
                    )
                )
            )

        val actual = orderFacade.productOrder(
            123L,
            listOf(
                OrderItemInfoDto(productDetailId = 1L, quantity = 1),
                OrderItemInfoDto(productDetailId = 2L, quantity = 2),
            )
        )

        assertThat(actual).isNotNull
        assertThat(actual.totalPrice).isEqualTo(20000)
    }
}