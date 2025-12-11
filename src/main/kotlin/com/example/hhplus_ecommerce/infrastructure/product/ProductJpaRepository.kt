package com.example.hhplus_ecommerce.infrastructure.product

import com.example.hhplus_ecommerce.infrastructure.product.entity.ProductEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProductJpaRepository : JpaRepository<ProductEntity, Long> {
	@Query("SELECT p FROM ProductEntity p")
	fun findAllWithPaging(pageable: Pageable): List<ProductEntity>

	fun findAllByIdIn(ids: List<Long>): List<ProductEntity>
}