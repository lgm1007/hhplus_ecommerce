package com.example.hhplus_ecommerce.domain.order.event

import com.example.hhplus_ecommerce.infrastructure.kafka.producer.MessageProducer
import com.example.hhplus_ecommerce.infrastructure.kafka.producer.dto.ProductMessage
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class OrderEventListener(
	private val messageProducer: MessageProducer
) {
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	fun productOrderEventListen(event: ProductOrderMessageEvent) {
		for (orderItemInfo in event.orderItemInfos) {
			messageProducer.sendProductOrderMessage(
				ProductMessage(
					orderItemInfo.productDetailId,
					orderItemInfo.quantity
				)
			)
		}
	}
}