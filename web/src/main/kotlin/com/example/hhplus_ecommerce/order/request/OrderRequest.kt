package com.example.hhplus_ecommerce.order.request

import com.example.hhplus_ecommerce.order.dto.OrderItemInfoDto

data class OrderRequest(
	val userId: Long,
	val orderItemInfos: List<OrderItemInfoDto>
)