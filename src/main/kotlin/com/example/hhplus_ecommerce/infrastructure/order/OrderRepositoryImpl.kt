package com.example.hhplus_ecommerce.infrastructure.order

import com.example.hhplus_ecommerce.domain.order.OrderRepository
import com.example.hhplus_ecommerce.domain.order.dto.OrderDto
import com.example.hhplus_ecommerce.infrastructure.order.entity.Order
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class OrderRepositoryImpl(private val orderJpaRepository: OrderJpaRepository) : OrderRepository {
	@Transactional
	override fun insert(orderDto: OrderDto): OrderDto {
		return OrderDto.from(orderJpaRepository.save(Order.from(orderDto)))
	}
}