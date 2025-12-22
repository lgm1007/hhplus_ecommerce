package com.example.hhplus_ecommerce.product

import com.example.hhplus_ecommerce.share.exception.BadRequestException
import com.example.hhplus_ecommerce.share.exception.ErrorStatus
import java.time.LocalDateTime

data class ProductDetail(
	val productDetailId: Long? = null,
	val productId: Long,
	val price: Int,
	var stockQuantity: Int,
	val productCategory: ProductCategory,
	var createdDate: LocalDateTime? = null,
	var lastModifiedDate: LocalDateTime? = null
) {
	fun decreaseQuantity(orderQuantity: Int) {
		if (this.stockQuantity < orderQuantity) {
			throw BadRequestException(ErrorStatus.NOT_ENOUGH_QUANTITY)
		}
		this.stockQuantity -= orderQuantity
	}
}
