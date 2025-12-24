package com.example.hhplus_ecommerce.product.response

import com.example.hhplus_ecommerce.product.dto.ProductInfoDto

data class ProductResponse(
	val items: List<ProductResponseItem>
) {
	companion object {
		fun from(productInfoDtos: List<ProductInfoDto>): ProductResponse {
			return ProductResponse(items = ProductResponseItem.fromList(productInfoDtos))
		}
	}
}