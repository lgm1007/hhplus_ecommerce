package com.example.hhplus_ecommerce.infrastructure.order

import com.example.hhplus_ecommerce.infrastructure.order.entity.Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderJpaRepository : JpaRepository<Order, Long> {
}