package com.example.hhplus_ecommerce.domain.product.dto

import com.example.hhplus_ecommerce.infrastructure.product.entity.ProductEntity
import java.time.LocalDateTime

class ProductDto(
	val id: Long,
	val name: String,
	val description: String,
	val createdDate: LocalDateTime
) {
	companion object {
		fun from(productEntity: ProductEntity): ProductDto {
			return ProductDto(
				productEntity.id,
				productEntity.name,
				productEntity.description,
				productEntity.createdDate
			)
		}

		fun fromList(productEntities: List<ProductEntity>): List<ProductDto> {
			return productEntities.map(::from)
		}
	}
}