package com.example.hhplus_ecommerce.product

import java.time.LocalDateTime

data class ProductDetail(
	val productDetailId: Long? = null,
	val productId: Long,
	val price: Int,
	var stockQuantity: Int,
	val productCategory: ProductCategory,
	var createdDate: LocalDateTime? = null,
	var lastModifiedDate: LocalDateTime? = null
)
