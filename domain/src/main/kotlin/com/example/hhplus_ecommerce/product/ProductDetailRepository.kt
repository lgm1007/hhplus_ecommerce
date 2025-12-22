package com.example.hhplus_ecommerce.product

interface ProductDetailRepository {
	fun insert(productDetail: ProductDetail): ProductDetail

	fun getAllByProductIdsIn(productIds: List<Long>): List<ProductDetail>

	fun getByProductId(productId: Long): ProductDetail

	fun getByIdWithWriteLock(id: Long): ProductDetail

	fun getAllByIdsInWithLock(ids: List<Long>): List<ProductDetail>

	fun getAllByIdsIn(ids: List<Long>): List<ProductDetail>

	fun updateProductQuantityDecreaseWithLock(id: Long, orderQuantity: Int): ProductDetail

	fun updateProductQuantityDecrease(id: Long, orderQuantity: Int): ProductDetail
}