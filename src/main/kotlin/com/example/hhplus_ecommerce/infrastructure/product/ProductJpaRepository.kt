package com.example.hhplus_ecommerce.infrastructure.product

import com.example.hhplus_ecommerce.infrastructure.product.entity.Product
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProductJpaRepository : JpaRepository<Product, Long> {
	@Query("SELECT p FROM Product p")
	fun findAllWithPaging(pageable: Pageable): List<Product>

	fun findAllByIdIn(ids: List<Long>): List<Product>
}