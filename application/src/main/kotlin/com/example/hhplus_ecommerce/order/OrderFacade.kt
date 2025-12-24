package com.example.hhplus_ecommerce.order

import com.example.hhplus_ecommerce.cart.CartService
import com.example.hhplus_ecommerce.order.dto.OrderEventInfoDto
import com.example.hhplus_ecommerce.order.dto.OrderInfoDto
import com.example.hhplus_ecommerce.order.dto.OrderItemDetailInfoDto
import com.example.hhplus_ecommerce.order.dto.OrderItemInfoDto
import com.example.hhplus_ecommerce.order.event.BeforeProductOrderEvent
import com.example.hhplus_ecommerce.order.event.ProductOrderMessageEvent
import com.example.hhplus_ecommerce.product.ProductService
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
	fun productOrder(userId: Long, orderItemInfoDtos: List<OrderItemInfoDto>): OrderInfoDto {
		val productDetailIds = orderItemInfoDtos.map { it.productDetailId }
		val productDetails = productService.getAllProductDetailsByDetailIdsInWithLock(productDetailIds)
		val orderItemDetailInfos = OrderItemDetailInfoDto.ofList(productDetails, orderItemInfoDtos)
		// 주문 정보 등록
		val (savedOrder, savedOrderItems) = orderService.doOrder(userId, orderItemDetailInfos)

		// 재고 확인 및 재고 차감
		orderItemInfoDtos.forEach { orderItemInfo ->
			productService.updateProductQuantityDecreaseWithDBLock(
				productDetailId = orderItemInfo.productDetailId,
				orderQuantity = orderItemInfo.quantity
			)
		}

		// 장바구니 삭제
		cartService.deleteCartByUser(userId)

		return OrderInfoDto.of(orderDto = savedOrder, orderItemDtos = savedOrderItems)
	}

	fun productOrderWithLettuce(userId: Long, orderItemInfoDtos: List<OrderItemInfoDto>): OrderInfoDto {
		val productDetailIds = orderItemInfoDtos.map { it.productDetailId }
		val productDetails = productService.getAllProductDetailsByIdsIn(productDetailIds)
		val orderItemDetailInfos = OrderItemDetailInfoDto.ofList(productDetails, orderItemInfoDtos)
		// 주문 정보 등록
		val (savedOrder, savedOrderItems) = orderService.doOrder(userId, orderItemDetailInfos)

		// 재고 확인 및 재고 차감
		for (orderItemInfo in orderItemInfoDtos) {
			productService.updateProductQuantityDecreaseWithLettuce(
				productDetailId = orderItemInfo.productDetailId,
				orderQuantity = orderItemInfo.quantity
			)
		}

		// 장바구니 삭제
		cartService.deleteCartByUser(userId)

		return OrderInfoDto.of(orderDto = savedOrder, orderItemDtos = savedOrderItems)
	}

	fun productOrderWithRedisson(userId: Long, orderItemInfoDtos: List<OrderItemInfoDto>): OrderInfoDto {
		val productDetailIds = orderItemInfoDtos.map { it.productDetailId }
		val productDetailDtos = productService.getAllProductDetailsByIdsIn(productDetailIds)
		val orderItemDetailInfoDtos = OrderItemDetailInfoDto.ofList(productDetailDtos, orderItemInfoDtos)
		// 주문 정보 등록
		val (savedOrder, savedOrderItems) = orderService.doOrder(userId, orderItemDetailInfoDtos)

		// 재고 확인 및 재고 차감
		for (orderItemInfo in orderItemInfoDtos) {
			productService.updateProductQuantityDecreaseWithRedisson(
				productDetailId = orderItemInfo.productDetailId,
				orderQuantity = orderItemInfo.quantity
			)
		}

		// 장바구니 삭제
		cartService.deleteCartByUser(userId)

		return OrderInfoDto.of(
			orderDto = savedOrder,
			orderItemDtos = savedOrderItems
		)
	}

	@Transactional
	fun productOrderWithKafka(userId: Long, orderItemInfoDtos: List<OrderItemInfoDto>): OrderInfoDto {
		// 주문 진행 전 이벤트 발행
		eventPublisher.publishEvent(
			BeforeProductOrderEvent(
				OrderEventInfoDto(
					userId = userId,
					orderItemInfoDtos = orderItemInfoDtos
				)
			)
		)

		val productDetailIds = orderItemInfoDtos.map { orderItemInfo -> orderItemInfo.productDetailId }
		val productDetails = productService.getAllProductDetailsByDetailIdsInWithLock(productDetailIds)
		val orderItemDetailInfoDtos = OrderItemDetailInfoDto.ofList(productDetails, orderItemInfoDtos)
		// 주문 정보 등록
		val (savedOrder, savedOrderItems) = orderService.doOrder(
			userId = userId,
			orderItemDetailInfoDtos = orderItemDetailInfoDtos
		)

		// 장바구니 삭제
		cartService.deleteCartByUser(userId)

		// 재고 차감 이벤트 발생
		eventPublisher.publishEvent(
			ProductOrderMessageEvent(
				OrderEventInfoDto(
					userId = userId,
					orderItemInfoDtos = orderItemInfoDtos
				)
			)
		)

		return OrderInfoDto.of(orderDto = savedOrder, orderItemDtos = savedOrderItems)
	}
}