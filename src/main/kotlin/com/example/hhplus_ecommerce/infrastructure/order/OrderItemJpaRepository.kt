package com.example.hhplus_ecommerce.infrastructure.order

import com.example.hhplus_ecommerce.domain.order.dto.OrderQuantityStatisticsInfo
import com.example.hhplus_ecommerce.infrastructure.order.entity.OrderItem
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface OrderItemJpaRepository : JpaRepository<OrderItem, Long> {
	@Query("SELECT new com.example.hhplus_ecommerce.domain.order.dto.OrderQuantityStatisticsInfo(oi.productDetailId, SUM(oi.quantity)) " +
		"FROM OrderItem oi " +
		"WHERE oi.createdDate >= :standardDate " +
		"GROUP BY oi.productDetailId " +
		"ORDER BY SUM(oi.quantity) DESC")
	fun findTopQuantityByCreatedDateMoreThan(@Param("standardDate") standardDate: LocalDateTime, pageable: Pageable): List<OrderQuantityStatisticsInfo>

	fun findAllByOrderId(orderId: Long): List<OrderItem>
}