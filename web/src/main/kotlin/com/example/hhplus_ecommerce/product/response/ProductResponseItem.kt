package com.example.hhplus_ecommerce.product.response

import com.example.hhplus_ecommerce.product.dto.ProductInfoDto

data class ProductResponseItem(
	val productId: Long,
	val name: String,
	val price: Int,
	val stockQuantity: Int
) {
	companion object {
		fun from(productInfoDto: ProductInfoDto): ProductResponseItem {
			return ProductResponseItem(
				productId = productInfoDto.productId,
				name = productInfoDto.name,
				price = productInfoDto.price,
				stockQuantity = productInfoDto.stockQuantity
			)
		}

		fun fromList(productInfoDtos: List<ProductInfoDto>): List<ProductResponseItem> {
			return productInfoDtos.map(::from)
		}
	}
}