package com.example.hhplus_ecommerce.product.dto

class ProductStatisticsInfoDto(
	val productId: Long,
	val productDetailId: Long,
	val name: String,
) {
	companion object {
		fun of(productDto: ProductDto, productDetailDto: ProductDetailDto): ProductStatisticsInfoDto {
			return ProductStatisticsInfoDto(
				productId = productDto.productId!!,
				productDetailId = productDetailDto.productDetailId!!,
				name = productDto.name
			)
		}

		fun listOf(productDtos: List<ProductDto>, productDetailDtos: List<ProductDetailDto>): List<ProductStatisticsInfoDto> {
			return productDtos.mapNotNull { product ->
				val productDetail = productDetailDtos.find { it.productId == product.productId }

				productDetail?.let { of(productDto = product, productDetailDto = productDetail) }
			}
		}
	}
}