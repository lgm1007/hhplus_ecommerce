package com.example.hhplus_ecommerce.product.dto

import com.example.hhplus_ecommerce.product.Product
import java.time.LocalDateTime

class ProductDto(
	val productId: Long? = null,
	val name: String,
	val description: String,
	val createdDate: LocalDateTime? = null
) {
	companion object {
		fun from(product: Product): ProductDto {
			return ProductDto(
				productId = product.productId,
				name = product.name,
				description = product.description,
				createdDate = product.createdDate
			)
		}

		fun fromList(productEntities: List<Product>): List<ProductDto> {
			return productEntities.map(::from)
		}
	}
}