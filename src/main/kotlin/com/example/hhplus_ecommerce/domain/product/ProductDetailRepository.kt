package com.example.hhplus_ecommerce.domain.product

import com.example.hhplus_ecommerce.domain.product.dto.ProductDetailDto
import com.example.hhplus_ecommerce.infrastructure.product.entity.ProductDetail

interface ProductDetailRepository {
	fun insert(productDetailDto: ProductDetailDto): ProductDetailDto

	fun getAllByProductIdsIn(productIds: List<Long>): List<ProductDetailDto>

	fun getByProductId(productId: Long): ProductDetailDto

	fun getByIdWithWriteLock(id: Long): ProductDetailDto

	fun getAllByIdsIn(ids: List<Long>): List<ProductDetail>

	fun updateProductQuantityDecrease(id: Long, orderQuantity: Int): ProductDetail
}