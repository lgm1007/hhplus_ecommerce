package com.example.hhplus_ecommerce.domain.product

import com.example.hhplus_ecommerce.domain.product.dto.ProductDetailDto
import com.example.hhplus_ecommerce.infrastructure.product.entity.ProductDetail

interface ProductDetailRepository {
	fun insert(productDetailDto: ProductDetailDto): ProductDetail

	fun getAllByProductIdsIn(productIds: List<Long>): List<ProductDetail>

	fun getByProductId(productId: Long): ProductDetail

	fun getByIdWithWriteLock(id: Long): ProductDetail

	fun getAllByIdsInWithLock(ids: List<Long>): List<ProductDetail>

	fun updateProductQuantityDecrease(id: Long, orderQuantity: Int): ProductDetail
}