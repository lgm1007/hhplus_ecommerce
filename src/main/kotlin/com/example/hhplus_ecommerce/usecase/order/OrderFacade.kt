package com.example.hhplus_ecommerce.usecase.order

import com.example.hhplus_ecommerce.domain.cart.CartService
import com.example.hhplus_ecommerce.domain.order.OrderService
import com.example.hhplus_ecommerce.domain.order.OrderStatus
import com.example.hhplus_ecommerce.domain.order.dto.*
import com.example.hhplus_ecommerce.domain.product.ProductService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class OrderFacade(
	private val orderService: OrderService,
	private val productService: ProductService,
	private val cartService: CartService
) {
	@Transactional
	fun productOrder(userId: Long, orderItemInfos: List<OrderItemInfo>): OrderInfo {
		// 재고 확인
		val productDetailIds = orderItemInfos.map { orderItemInfo -> orderItemInfo.productDetailId }
		val productDetails = productService.getAllProductDetailsByDetailIdsInWithLock(productDetailIds)
		val orderItemDetailInfos = OrderItemDetailInfo.ofList(productDetails, orderItemInfos)
		val totalPrices = orderItemDetailInfos.map { orderItemDetailInfo -> orderItemDetailInfo.calculateOrderPrice() }
		val orderDto = OrderDto.from(userId)

		// 주문 정보 저장
		orderDto.addTotalPrice(totalPrices)
		orderDto.updateOrderStatus(OrderStatus.ORDER_COMPLETE)
		val registerOrder = orderService.registerOrder(orderDto)

		val orderItemDtos = OrderItemDto.listOf(registerOrder.id, orderItemDetailInfos)
		val registerOrderItems = orderService.registerOrderItems(orderItemDtos)

		// 재고 차감
		orderItemInfos.forEach { orderItemInfo ->
			productService.updateProductQuantityDecrease(
				orderItemInfo.productDetailId, orderItemInfo.quantity
			)
		}

		// 장바구니 삭제
		cartService.deleteCartByUser(userId)

		return OrderInfo.of(registerOrder, registerOrderItems)
	}
}