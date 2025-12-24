package com.example.hhplus_ecommerce.order

import com.example.hhplus_ecommerce.order.entity.OrderItemEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface OrderItemJpaRepository : JpaRepository<OrderItemEntity, Long> {
	@Query("SELECT new com.example.hhplus_ecommerce.order.OrderQuantityStatistics(oi.productDetailId, SUM(oi.quantity)) " +
			"FROM OrderItemEntity oi " +
		"WHERE oi.createdDate >= :standardDate " +
		"GROUP BY oi.productDetailId " +
		"ORDER BY SUM(oi.quantity) DESC")
	fun findTopQuantityByCreatedDateMoreThan(@Param("standardDate") standardDate: LocalDateTime, pageable: Pageable): List<OrderQuantityStatistics>

	fun findAllByOrderId(orderId: Long): List<OrderItemEntity>
}