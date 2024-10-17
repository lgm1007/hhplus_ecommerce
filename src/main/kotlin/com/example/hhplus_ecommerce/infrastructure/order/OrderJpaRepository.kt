package com.example.hhplus_ecommerce.infrastructure.order

import com.example.hhplus_ecommerce.infrastructure.order.entity.OrderTable
import org.springframework.data.jpa.repository.JpaRepository

interface OrderJpaRepository : JpaRepository<OrderTable, Long> {
}