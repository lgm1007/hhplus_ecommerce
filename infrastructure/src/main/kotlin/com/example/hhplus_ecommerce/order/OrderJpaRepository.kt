package com.example.hhplus_ecommerce.order

import com.example.hhplus_ecommerce.order.entity.OrderEntity
import org.springframework.data.jpa.repository.JpaRepository

interface OrderJpaRepository : JpaRepository<OrderEntity, Long> {
}