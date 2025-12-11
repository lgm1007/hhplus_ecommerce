package com.example.hhplus_ecommerce.web.product.response

import com.example.hhplus_ecommerce.domain.product.dto.ProductInfo

class ProductResponse(
	val items: List<ProductResponseItem>
) {
	companion object {
		fun from(productInfos: List<ProductInfo>): ProductResponse {
			return ProductResponse(ProductResponseItem.fromList(productInfos))
		}
	}
}