package com.example.hhplus_ecommerce.product.dto

import com.example.hhplus_ecommerce.product.ProductCategory
import com.example.hhplus_ecommerce.product.ProductDetail
import java.time.LocalDateTime

class ProductDetailDto(
	val productDetailId: Long? = null,
	val productId: Long,
	val price: Int,
	var stockQuantity: Int,
	val productCategory: ProductCategory,
	val createdDate: LocalDateTime? = null,
	val lastModifiedDate: LocalDateTime? = null,
) {
	companion object {
		fun from(productDetail: ProductDetail): ProductDetailDto {
			return ProductDetailDto(
				productDetailId = productDetail.productDetailId,
				productId = productDetail.productId,
				price = productDetail.price,
				stockQuantity = productDetail.stockQuantity,
				productCategory = productDetail.productCategory,
				createdDate = productDetail.createdDate,
				lastModifiedDate = productDetail.lastModifiedDate
			)
		}

		fun fromList(productDetailEntities: List<ProductDetail>): List<ProductDetailDto> {
			return productDetailEntities.map(::from)
		}
	}
}