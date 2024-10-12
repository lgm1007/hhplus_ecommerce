package com.example.hhplus_ecommerce.infrastructure.product

import com.example.hhplus_ecommerce.domain.product.ProductRepository
import com.example.hhplus_ecommerce.domain.product.dto.ProductDto
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl(
	val productJpaRepository: ProductJpaRepository
) : ProductRepository {
	override fun getAllByPaging(pageable: Pageable): List<ProductDto> {
		return productJpaRepository.findAllWithPaging(pageable)
			.map(ProductDto.Companion::from)
	}
}