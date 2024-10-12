package com.example.hhplus_ecommerce.infrastructure.product

import com.example.hhplus_ecommerce.domain.product.ProductDetailRepository
import com.example.hhplus_ecommerce.domain.product.dto.ProductDetailDto
import org.springframework.stereotype.Repository

@Repository
class ProductDetailRepositoryImpl(
	private val productDetailJpaRepository: ProductDetailJpaRepository
) : ProductDetailRepository {
	override fun getAllByProductIdsIn(productIds: List<Long>): List<ProductDetailDto> {
		return productDetailJpaRepository.findAllByProductId(productIds)
			.map(ProductDetailDto.Companion::from)
	}
}