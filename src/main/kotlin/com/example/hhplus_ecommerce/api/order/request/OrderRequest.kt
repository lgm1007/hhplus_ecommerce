package com.example.hhplus_ecommerce.api.order.request

import com.example.hhplus_ecommerce.domain.order.dto.OrderItemInfo

class OrderRequest(
	val userId: Long,
	val orderItemInfos: List<OrderItemInfo>
) {
}