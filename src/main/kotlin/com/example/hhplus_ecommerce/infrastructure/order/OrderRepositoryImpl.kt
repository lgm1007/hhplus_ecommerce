package com.example.hhplus_ecommerce.infrastructure.order

import com.example.hhplus_ecommerce.domain.order.OrderRepository
import com.example.hhplus_ecommerce.domain.order.dto.OrderDto
import com.example.hhplus_ecommerce.infrastructure.order.entity.Order
import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryImpl(private val orderJpaRepository: OrderJpaRepository) : OrderRepository {
	override fun insert(orderDto: OrderDto): OrderDto {
		return OrderDto.from(orderJpaRepository.save(Order.from(orderDto)))
	}
}