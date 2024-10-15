package com.example.hhplus_ecommerce.infrastructure.product

import com.example.hhplus_ecommerce.api.error.ErrorStatus
import com.example.hhplus_ecommerce.domain.product.ProductDetailRepository
import com.example.hhplus_ecommerce.domain.product.dto.ProductDetailDto
import com.example.hhplus_ecommerce.exception.NotFoundException
import com.example.hhplus_ecommerce.infrastructure.product.entity.ProductDetail
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class ProductDetailRepositoryImpl(
	private val productDetailJpaRepository: ProductDetailJpaRepository
) : ProductDetailRepository {
	override fun insert(productDetailDto: ProductDetailDto): ProductDetailDto {
		return ProductDetailDto.from(productDetailJpaRepository.save(ProductDetail.from(productDetailDto)))
	}

	override fun getAllByProductIdsIn(productIds: List<Long>): List<ProductDetailDto> {
		return productDetailJpaRepository.findAllByProductIdIn(productIds)
			.map(ProductDetailDto.Companion::from)
	}

	@Transactional(readOnly = true)
	override fun getByProductIdWithReadLock(productId: Long): ProductDetailDto {
		val productDetail = (productDetailJpaRepository.findByProductIdWithReadLock(productId)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_PRODUCT))

		return ProductDetailDto.from(productDetail)
	}

	@Transactional
	override fun getByIdWithWriteLock(id: Long): ProductDetailDto {
		val productDetail = productDetailJpaRepository.findByIdWithWriteLock(id)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_PRODUCT)

		return ProductDetailDto.from(productDetail)
	}

	override fun getAllByIdsIn(ids: List<Long>): List<ProductDetail> {
		return productDetailJpaRepository.findAllByIdIn(ids)
	}

	@Transactional
	override fun updateProductQuantityDecrease(id: Long, orderQuantity: Int): ProductDetail {
		val productDetail = (productDetailJpaRepository.findByIdWithWriteLock(id)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_PRODUCT))

		productDetail.decreaseQuantity(orderQuantity)

		return productDetailJpaRepository.save(productDetail)
	}
}