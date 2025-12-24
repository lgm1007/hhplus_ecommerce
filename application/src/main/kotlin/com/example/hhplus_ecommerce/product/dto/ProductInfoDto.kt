package com.example.hhplus_ecommerce.product.dto

class ProductInfoDto(
	val productId: Long,
	val name: String,
	val price: Int,
	val stockQuantity: Int
) {
	companion object {
		fun of(product: ProductDto, productDetail: ProductDetailDto): ProductInfoDto {
			return ProductInfoDto(
				productDetail.productId,
				product.name,
				productDetail.price,
				productDetail.stockQuantity,
			)
		}

		fun listOf(products: List<ProductDto>, productDetails: List<ProductDetailDto>): List<ProductInfoDto> {
			return products.mapNotNull { product ->
				val productDetail = productDetails.find { it.productId == product.productId }

				productDetail?.let { productDetail -> of(product, productDetail) }
			}
		}
	}
}