package com.example.hhplus_ecommerce.domain.order.event

import com.example.hhplus_ecommerce.domain.order.dto.OrderItemInfo
import org.springframework.context.ApplicationEvent

class ProductOrderMessageEvent(
	val orderItemInfos: List<OrderItemInfo>
) : ApplicationEvent(orderItemInfos)