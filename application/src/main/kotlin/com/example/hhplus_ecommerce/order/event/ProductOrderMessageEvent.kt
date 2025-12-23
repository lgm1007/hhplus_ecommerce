package com.example.hhplus_ecommerce.order.event

import com.example.hhplus_ecommerce.order.dto.OrderEventInfoDto
import org.springframework.context.ApplicationEvent

class ProductOrderMessageEvent(
	val orderEventInfo: OrderEventInfoDto
) : ApplicationEvent(orderEventInfo) {}