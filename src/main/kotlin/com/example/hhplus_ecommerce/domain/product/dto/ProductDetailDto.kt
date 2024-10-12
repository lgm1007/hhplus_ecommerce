package com.example.hhplus_ecommerce.domain.product.dto

import com.example.hhplus_ecommerce.domain.product.ProductCategory
import com.example.hhplus_ecommerce.infrastructure.product.entity.ProductDetail
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

class ProductDetailDto(
	val id: Long,
	val productId: Long,
	val stockQuantity: Int,
	val productCategory: ProductCategory,
	@CreatedDate
	val createdDate: LocalDateTime,
	@LastModifiedDate
	val lastModifiedDate: LocalDateTime,
) {
	companion object {
		fun from(productDetail: ProductDetail): ProductDetailDto {
			return ProductDetailDto(
				productDetail.id,
				productDetail.productId,
				productDetail.stockQuantity,
				productDetail.productCategory,
				productDetail.createdDate,
				productDetail.lastModifiedDate
			)
		}
	}
}