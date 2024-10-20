package com.example.hhplus_ecommerce.domain.product

import com.example.hhplus_ecommerce.domain.product.dto.ProductDto
import com.example.hhplus_ecommerce.infrastructure.product.entity.Product
import org.springframework.data.domain.Pageable

interface ProductRepository {
	fun insert(productDto: ProductDto): Product

	fun getAllByPaging(pageable: Pageable): List<Product>

	fun getById(id: Long): Product

	fun getAllByIds(ids: List<Long>): List<Product>
}