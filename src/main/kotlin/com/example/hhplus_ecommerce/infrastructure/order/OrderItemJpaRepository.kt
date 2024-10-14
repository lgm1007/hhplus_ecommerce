package com.example.hhplus_ecommerce.infrastructure.order

import com.example.hhplus_ecommerce.infrastructure.order.entity.OrderItem
import org.springframework.data.jpa.repository.JpaRepository

interface OrderItemJpaRepository : JpaRepository<OrderItem, Long> {
}