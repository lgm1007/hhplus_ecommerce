package com.example.hhplus_ecommerce.domain.product.dto

class ProductStatisticsInfo(
	val productId: Long,
	val productDetailId: Long,
	val name: String,
) {
	companion object {
		fun of(productDto: ProductDto, productDetailDto: ProductDetailDto): ProductStatisticsInfo {
			return ProductStatisticsInfo(
				productDto.id,
				productDetailDto.id,
				productDto.name
			)
		}

		fun listOf(productDtos: List<ProductDto>, productDetailDtos: List<ProductDetailDto>): List<ProductStatisticsInfo> {
			return productDtos.mapNotNull { product ->
				val productDetail = productDetailDtos.find { it.productId == product.id }

				productDetail?.let { of(product, productDetail) }
			}
		}
	}
}