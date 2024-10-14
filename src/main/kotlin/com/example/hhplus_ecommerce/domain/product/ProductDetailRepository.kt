package com.example.hhplus_ecommerce.domain.product

import com.example.hhplus_ecommerce.domain.product.dto.ProductDetailDto

interface ProductDetailRepository {
	fun insert(productDetailDto: ProductDetailDto): ProductDetailDto

	fun getAllByProductIdsIn(productIds: List<Long>): List<ProductDetailDto>

	fun getByProductIdWithReadLock(productId: Long): ProductDetailDto
}