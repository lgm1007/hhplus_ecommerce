package com.example.hhplus_ecommerce.infrastructure.order

import com.example.hhplus_ecommerce.domain.share.exception.ErrorStatus
import com.example.hhplus_ecommerce.domain.order.OrderRepository
import com.example.hhplus_ecommerce.domain.order.OrderStatus
import com.example.hhplus_ecommerce.domain.order.dto.OrderDto
import com.example.hhplus_ecommerce.domain.share.exception.NotFoundException
import com.example.hhplus_ecommerce.infrastructure.order.entity.OrderTableEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryImpl(private val orderJpaRepository: OrderJpaRepository) : OrderRepository {
	override fun insert(orderDto: OrderDto): OrderTableEntity {
		return orderJpaRepository.save(OrderTableEntity.from(orderDto))
	}

	override fun getById(orderId: Long): OrderTableEntity {
		return orderJpaRepository.findByIdOrNull(orderId)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_ORDER)
	}

	override fun updateOrderStatus(orderId: Long, orderStatus: OrderStatus): OrderTableEntity {
		val order = (orderJpaRepository.findByIdOrNull(orderId)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_ORDER))

		order.updateStatus(orderStatus)
		return orderJpaRepository.save(order)
	}
}