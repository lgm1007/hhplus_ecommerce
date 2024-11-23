package com.example.hhplus_ecommerce.usecase.order

import com.example.hhplus_ecommerce.domain.cart.CartService
import com.example.hhplus_ecommerce.domain.order.OrderService
import com.example.hhplus_ecommerce.domain.order.dto.OrderEventInfo
import com.example.hhplus_ecommerce.domain.order.dto.OrderInfo
import com.example.hhplus_ecommerce.domain.order.dto.OrderItemDetailInfo
import com.example.hhplus_ecommerce.domain.order.dto.OrderItemInfo
import com.example.hhplus_ecommerce.domain.order.event.BeforeProductOrderEvent
import com.example.hhplus_ecommerce.domain.order.event.ProductOrderMessageEvent
import com.example.hhplus_ecommerce.domain.product.ProductService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class OrderFacade(
	private val orderService: OrderService,
	private val productService: ProductService,
	private val cartService: CartService,
	private val eventPublisher: ApplicationEventPublisher
) {
	@Transactional
	fun productOrder(userId: Long, orderItemInfos: List<OrderItemInfo>): OrderInfo {
		val productDetailIds = orderItemInfos.map { orderItemInfo -> orderItemInfo.productDetailId }
		val productDetails = productService.getAllProductDetailsByDetailIdsInWithLock(productDetailIds)
		val orderItemDetailInfos = OrderItemDetailInfo.ofList(productDetails, orderItemInfos)
		// 주문 정보 등록
		val (savedOrder, savedOrderItems) = orderService.doOrder(userId, orderItemDetailInfos)

		// 재고 확인 및 재고 차감
		orderItemInfos.forEach { orderItemInfo ->
			productService.updateProductQuantityDecreaseWithDBLock(
				orderItemInfo.productDetailId, orderItemInfo.quantity
			)
		}

		// 장바구니 삭제
		cartService.deleteCartByUser(userId)

		return OrderInfo.of(savedOrder, savedOrderItems)
	}

	fun productOrderWithLettuce(userId: Long, orderItemInfos: List<OrderItemInfo>): OrderInfo {
		val productDetailIds = orderItemInfos.map { orderItemInfo -> orderItemInfo.productDetailId }
		val productDetails = productService.getAllProductDetailsByIdsIn(productDetailIds)
		val orderItemDetailInfos = OrderItemDetailInfo.ofList(productDetails, orderItemInfos)
		// 주문 정보 등록
		val (savedOrder, savedOrderItems) = orderService.doOrder(userId, orderItemDetailInfos)

		// 재고 확인 및 재고 차감
		for (orderItemInfo in orderItemInfos) {
			productService.updateProductQuantityDecreaseWithLettuce(
				orderItemInfo.productDetailId, orderItemInfo.quantity
			)
		}

		// 장바구니 삭제
		cartService.deleteCartByUser(userId)

		return OrderInfo.of(savedOrder, savedOrderItems)
	}

	fun productOrderWithRedisson(userId: Long, orderItemInfos: List<OrderItemInfo>): OrderInfo {
		val productDetailIds = orderItemInfos.map { orderItemInfo -> orderItemInfo.productDetailId }
		val productDetails = productService.getAllProductDetailsByIdsIn(productDetailIds)
		val orderItemDetailInfos = OrderItemDetailInfo.ofList(productDetails, orderItemInfos)
		// 주문 정보 등록
		val (savedOrder, savedOrderItems) = orderService.doOrder(userId, orderItemDetailInfos)

		// 재고 확인 및 재고 차감
		for (orderItemInfo in orderItemInfos) {
			productService.updateProductQuantityDecreaseWithRedisson(
				orderItemInfo.productDetailId, orderItemInfo.quantity
			)
		}

		// 장바구니 삭제
		cartService.deleteCartByUser(userId)

		return OrderInfo.of(savedOrder, savedOrderItems)
	}

	@Transactional
	fun productOrderWithKafka(userId: Long, orderItemInfos: List<OrderItemInfo>): OrderInfo {
		// 주문 진행 전 이벤트 발행
		eventPublisher.publishEvent(BeforeProductOrderEvent(
			OrderEventInfo(userId, orderItemInfos)
		))

		val productDetailIds = orderItemInfos.map { orderItemInfo -> orderItemInfo.productDetailId }
		val productDetails = productService.getAllProductDetailsByDetailIdsInWithLock(productDetailIds)
		val orderItemDetailInfos = OrderItemDetailInfo.ofList(productDetails, orderItemInfos)
		// 주문 정보 등록
		val (savedOrder, savedOrderItems) = orderService.doOrder(userId, orderItemDetailInfos)

		// 장바구니 삭제
		cartService.deleteCartByUser(userId)

		// 재고 차감 이벤트 발생
		eventPublisher.publishEvent(ProductOrderMessageEvent(
			OrderEventInfo(userId, orderItemInfos)
		))

		return OrderInfo.of(savedOrder, savedOrderItems)
	}
}