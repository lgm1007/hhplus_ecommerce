package com.example.hhplus_ecommerce.domain.product.dto

import com.example.hhplus_ecommerce.infrastructure.product.entity.Product
import java.time.LocalDateTime

class ProductDto(
	val id: Long,
	val name: String,
	val description: String,
	val createdDate: LocalDateTime
) {
	companion object {
		fun from(product: Product): ProductDto {
			return ProductDto(
				product.id,
				product.name,
				product.description,
				product.createdDate
			)
		}

		fun fromList(products: List<Product>): List<ProductDto> {
			return products.map(::from)
		}
	}
}