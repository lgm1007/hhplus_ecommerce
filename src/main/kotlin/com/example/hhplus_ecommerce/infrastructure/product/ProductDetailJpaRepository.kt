package com.example.hhplus_ecommerce.infrastructure.product

import com.example.hhplus_ecommerce.infrastructure.product.entity.ProductDetail
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import javax.persistence.LockModeType

interface ProductDetailJpaRepository : JpaRepository<ProductDetail, Long> {
	fun findAllByProductIdIn(productIds: List<Long>): List<ProductDetail>

	fun findByProductId(productId: Long): ProductDetail?

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT pd FROM ProductDetail pd WHERE pd.id = :id")
	fun findByIdWithLock(@Param("id") id: Long): ProductDetail?

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT pd FROM ProductDetail pd WHERE pd.id IN (:ids)")
	fun findAllByIdInWithLock(ids: List<Long>): List<ProductDetail>
}