package com.example.hhplus_ecommerce.event

import com.example.hhplus_ecommerce.infrastructure.message.producer.MessageProducer
import com.example.hhplus_ecommerce.infrastructure.message.producer.dto.ProductMessage
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class MessageProduceEventListener(
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