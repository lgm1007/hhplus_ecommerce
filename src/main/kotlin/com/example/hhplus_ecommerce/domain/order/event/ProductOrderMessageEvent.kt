package com.example.hhplus_ecommerce.domain.order.event

import com.example.hhplus_ecommerce.domain.order.dto.OrderEventInfo
import org.springframework.context.ApplicationEvent

class ProductOrderMessageEvent(
	val orderEventInfo: OrderEventInfo
) : ApplicationEvent(orderEventInfo) {}