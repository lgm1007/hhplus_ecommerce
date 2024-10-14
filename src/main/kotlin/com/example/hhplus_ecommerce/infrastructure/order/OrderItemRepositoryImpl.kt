package com.example.hhplus_ecommerce.infrastructure.order

import com.example.hhplus_ecommerce.domain.order.OrderItemRepository
import com.example.hhplus_ecommerce.domain.order.dto.OrderItemDto
import com.example.hhplus_ecommerce.infrastructure.order.entity.OrderItem
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class OrderItemRepositoryImpl(private val orderItemJpaRepository: OrderItemJpaRepository) : OrderItemRepository {
	@Transactional
	override fun insert(orderItemDto: OrderItemDto): OrderItemDto {
		return OrderItemDto.from(orderItemJpaRepository.save(OrderItem.from(orderItemDto)))
	}

	@Transactional
	override fun insertAll(orderItemDtos: List<OrderItemDto>): List<OrderItemDto> {
		return orderItemJpaRepository.saveAll(orderItemDtos.map { orderItemDto ->
				OrderItem.from(orderItemDto)
			}).map { orderItem ->
				OrderItemDto.from(orderItem)
			}
	}
}