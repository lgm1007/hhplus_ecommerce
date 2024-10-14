package com.example.hhplus_ecommerce.domain.order.dto

class OrderItemDetailInfo(
	val productDetailId: Long,
	val quantity: Int,
	val price: Int
) {
	companion object {
		fun from(orderItemDto: OrderItemDto): OrderItemDetailInfo {
			return OrderItemDetailInfo(
				orderItemDto.productDetailId,
				orderItemDto.quantity,
				orderItemDto.price
			)
		}
	}
}