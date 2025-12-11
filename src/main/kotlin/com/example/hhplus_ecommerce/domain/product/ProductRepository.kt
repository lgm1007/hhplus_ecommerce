package com.example.hhplus_ecommerce.domain.product

import com.example.hhplus_ecommerce.domain.product.dto.ProductDto
import com.example.hhplus_ecommerce.infrastructure.product.entity.ProductEntity
import org.springframework.data.domain.Pageable

interface ProductRepository {
	fun insert(productDto: ProductDto): ProductEntity

	fun getAllByPaging(pageable: Pageable): List<ProductEntity>

	fun getById(id: Long): ProductEntity

	fun getAllByIds(ids: List<Long>): List<ProductEntity>
}