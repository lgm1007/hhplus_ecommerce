package com.example.hhplus_ecommerce.infrastructure.product

import com.example.hhplus_ecommerce.infrastructure.product.entity.ProductDetailEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import javax.persistence.LockModeType

interface ProductDetailJpaRepository : JpaRepository<ProductDetailEntity, Long> {
	fun findAllByProductIdIn(productIds: List<Long>): List<ProductDetailEntity>

	fun findByProductId(productId: Long): ProductDetailEntity?

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT pd FROM ProductDetailEntity pd WHERE pd.id = :id")
	fun findByIdWithLock(@Param("id") id: Long): ProductDetailEntity?

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT pd FROM ProductDetailEntity pd WHERE pd.id IN (:ids)")
	fun findAllByIdInWithLock(@Param("ids") ids: List<Long>): List<ProductDetailEntity>

	fun findAllByIdIn(ids: List<Long>): List<ProductDetailEntity>
}