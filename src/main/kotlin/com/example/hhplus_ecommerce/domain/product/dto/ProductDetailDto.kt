package com.example.hhplus_ecommerce.domain.product.dto

import com.example.hhplus_ecommerce.api.error.ErrorStatus
import com.example.hhplus_ecommerce.domain.product.ProductCategory
import com.example.hhplus_ecommerce.exception.BadRequestException
import com.example.hhplus_ecommerce.infrastructure.product.entity.ProductDetail
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

class ProductDetailDto(
	val id: Long,
	val productId: Long,
	val price: Int,
	var stockQuantity: Int,
	val productCategory: ProductCategory,
	@CreatedDate
	val createdDate: LocalDateTime,
	@LastModifiedDate
	val lastModifiedDate: LocalDateTime,
) {
	fun decreaseQuantity(orderQuantity: Int) {
		if (stockQuantity < orderQuantity) {
			throw BadRequestException(ErrorStatus.NOT_ENOUGH_QUANTITY)
		}

		stockQuantity -= orderQuantity
	}

	companion object {
		fun from(productDetail: ProductDetail): ProductDetailDto {
			return ProductDetailDto(
				productDetail.id,
				productDetail.productId,
				productDetail.price,
				productDetail.stockQuantity,
				productDetail.productCategory,
				productDetail.createdDate,
				productDetail.lastModifiedDate
			)
		}

		fun fromList(productDetails: List<ProductDetail>): List<ProductDetailDto> {
			return productDetails.map(::from)
		}
	}
}