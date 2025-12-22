package com.example.hhplus_ecommerce.product

import com.example.hhplus_ecommerce.domain.product.dto.ProductDetailDto
import com.example.hhplus_ecommerce.infrastructure.product.entity.ProductDetailEntity

interface ProductDetailRepository {
	fun insert(productDetailDto: ProductDetailDto): ProductDetailEntity

	fun getAllByProductIdsIn(productIds: List<Long>): List<ProductDetailEntity>

	fun getByProductId(productId: Long): ProductDetailEntity

	fun getByIdWithWriteLock(id: Long): ProductDetailEntity

	fun getAllByIdsInWithLock(ids: List<Long>): List<ProductDetailEntity>

	fun getAllByIdsIn(ids: List<Long>): List<ProductDetailEntity>

	fun updateProductQuantityDecreaseWithLock(id: Long, orderQuantity: Int): ProductDetailEntity

	fun updateProductQuantityDecrease(id: Long, orderQuantity: Int): ProductDetailEntity
}