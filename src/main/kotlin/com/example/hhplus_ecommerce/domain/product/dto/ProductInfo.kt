package com.example.hhplus_ecommerce.domain.product.dto

class ProductInfo(
	val productId: Long,
	val name: String,
	val price: Int,
	val stockQuantity: Int
) {
	companion object {
		fun of(product: ProductDto, productDetail: ProductDetailDto): ProductInfo {
			return ProductInfo(
				productDetail.productId,
				product.name,
				productDetail.price,
				productDetail.stockQuantity,
			)
		}

		fun listOf(products: List<ProductDto>, productDetails: List<ProductDetailDto>): List<ProductInfo> {
			return products.mapNotNull { product ->
				val productDetail = productDetails.find { it.productId == product.id }

				productDetail?.let { productDetail -> of(product, productDetail) }
			}
		}
	}
}