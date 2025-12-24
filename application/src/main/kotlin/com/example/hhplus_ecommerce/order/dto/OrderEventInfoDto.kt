package com.example.hhplus_ecommerce.order.dto

data class OrderEventInfoDto(
	val userId: Long,
	val orderItemInfoDtos: List<OrderItemInfoDto>
) {
	companion object {
		fun of(userId: Long, orderItemInfoDtos: List<OrderItemInfoDto>): OrderEventInfoDto {
			return OrderEventInfoDto(userId, orderItemInfoDtos)
		}
	}
}