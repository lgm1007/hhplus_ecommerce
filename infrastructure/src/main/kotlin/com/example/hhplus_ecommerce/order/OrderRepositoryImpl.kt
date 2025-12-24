package com.example.hhplus_ecommerce.order

import com.example.hhplus_ecommerce.order.entity.OrderEntity
import com.example.hhplus_ecommerce.share.exception.ErrorStatus
import com.example.hhplus_ecommerce.share.exception.NotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class OrderRepositoryImpl(private val orderJpaRepository: OrderJpaRepository) : OrderRepository {
	override fun insert(order: Order): Order {
		return orderJpaRepository.save(OrderEntity.from(order))
			.toDomain()
	}

	override fun getById(orderId: Long): Order {
		val findEntity =  orderJpaRepository.findByIdOrNull(orderId)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_ORDER)

		return findEntity.toDomain()
	}

	@Transactional
	override fun updateOrderStatus(orderId: Long, orderStatus: OrderStatus): Order {
		val findEntity =  orderJpaRepository.findByIdOrNull(orderId)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_ORDER)
		val order = findEntity.toDomain()

		order.updateOrderStatus(orderStatus)
		findEntity.orderStatus = order.orderStatus

		return order
	}
}