package com.example.hhplus_ecommerce.domain.order.dto

import com.example.hhplus_ecommerce.infrastructure.order.entity.OrderItemEntity
import java.time.LocalDateTime

class OrderItemDto(
	val id: Long,
	val orderId: Long,
	val productDetailId: Long,
	val quantity: Int,
	val price: Int,
	val createdDate: LocalDateTime
) {
	companion object {
		fun from(orderItemEntity: OrderItemEntity): OrderItemDto {
			return OrderItemDto(
				orderItemEntity.id,
				orderItemEntity.orderId,
				orderItemEntity.productDetailId,
				orderItemEntity.quantity,
				orderItemEntity.price,
				orderItemEntity.createdDate
			)
		}

		fun fromList(orderItemEntities: List<OrderItemEntity>): List<OrderItemDto> {
			return orderItemEntities.map(::from)
		}

		fun listOf(orderId: Long, orderItemDetailInfos: List<OrderItemDetailInfo>): List<OrderItemDto> {
			return orderItemDetailInfos.map { orderItemDetailInfo ->
				OrderItemDto(
					0,
					orderId,
					orderItemDetailInfo.productDetailId,
					orderItemDetailInfo.quantity,
					orderItemDetailInfo.price,
					LocalDateTime.now()
				)
			}
		}
	}
}