package com.example.hhplus_ecommerce.infrastructure.product

import com.example.hhplus_ecommerce.infrastructure.product.entity.ProductDetail
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import javax.persistence.LockModeType

interface ProductDetailJpaRepository : JpaRepository<ProductDetail, Long> {
	fun findAllByProductIdIn(productIds: List<Long>): List<ProductDetail>

	@Lock(LockModeType.PESSIMISTIC_READ)
	@Query("SELECT pd FROM ProductDetail pd WHERE pd.productId = :productId")
	fun findByProductIdWithReadLock(@Param("productId") productId: Long): ProductDetail?
}