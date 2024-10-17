package com.example.hhplus_ecommerce.api.product.response

import com.example.hhplus_ecommerce.domain.product.dto.ProductInfo

class ProductResponseItem(
	val id: Long,
	val name: String,
	val price: Int,
	val stockQuantity: Int
) {
	companion object {
		fun from(productInfo: ProductInfo): ProductResponseItem {
			return ProductResponseItem(
				productInfo.productId,
				productInfo.name,
				productInfo.price,
				productInfo.stockQuantity
			)
		}

		fun fromList(productInfos: List<ProductInfo>): List<ProductResponseItem> {
			return productInfos.map(::from)
		}
	}
}