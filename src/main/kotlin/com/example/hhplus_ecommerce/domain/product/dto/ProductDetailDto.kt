package com.example.hhplus_ecommerce.domain.product.dto

import com.example.hhplus_ecommerce.domain.product.ProductCategory
import com.example.hhplus_ecommerce.infrastructure.product.entity.ProductDetailEntity
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

class ProductDetailDto(
	val id: Long,
	val productId: Long,
	val price: Int,
	var stockQuantity: Int,
	val productCategory: ProductCategory,
	@CreatedDate
	val createdDate: LocalDateTime,
	@LastModifiedDate
	val lastModifiedDate: LocalDateTime,
) {
	companion object {
		fun from(productDetailEntity: ProductDetailEntity): ProductDetailDto {
			return ProductDetailDto(
				productDetailEntity.id,
				productDetailEntity.productId,
				productDetailEntity.price,
				productDetailEntity.stockQuantity,
				productDetailEntity.productCategory,
				productDetailEntity.createdDate,
				productDetailEntity.lastModifiedDate
			)
		}

		fun fromList(productDetailEntities: List<ProductDetailEntity>): List<ProductDetailDto> {
			return productDetailEntities.map(::from)
		}
	}
}