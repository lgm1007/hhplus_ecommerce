package com.example.hhplus_ecommerce.domain.product

import com.example.hhplus_ecommerce.domain.product.dto.ProductDto
import org.springframework.data.domain.Pageable

interface ProductRepository {
	fun insert(productDto: ProductDto): ProductDto

	fun getAllByPaging(pageable: Pageable): List<ProductDto>

	fun getById(id: Long): ProductDto
}