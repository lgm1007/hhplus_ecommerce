package com.example.hhplus_ecommerce.infrastructure.product

import com.example.hhplus_ecommerce.api.error.ErrorStatus
import com.example.hhplus_ecommerce.domain.product.ProductDetailRepository
import com.example.hhplus_ecommerce.domain.product.dto.ProductDetailDto
import com.example.hhplus_ecommerce.exception.NotFoundException
import org.springframework.stereotype.Repository

@Repository
class ProductDetailRepositoryImpl(
	private val productDetailJpaRepository: ProductDetailJpaRepository
) : ProductDetailRepository {
	override fun getAllByProductIdsIn(productIds: List<Long>): List<ProductDetailDto> {
		return productDetailJpaRepository.findAllByProductIdIn(productIds)
			.map(ProductDetailDto.Companion::from)
	}

	override fun getByProductIdWithReadLock(productId: Long): ProductDetailDto {
		val productDetail = (productDetailJpaRepository.findByProductIdWithReadLock(productId)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_PRODUCT))

		return ProductDetailDto.from(productDetail)
	}
}