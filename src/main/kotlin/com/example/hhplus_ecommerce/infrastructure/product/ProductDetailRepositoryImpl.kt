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
	override fun insert(productDetailDto: ProductDetailDto): ProductDetail {
		return productDetailJpaRepository.save(ProductDetail.from(productDetailDto))
	}

	override fun getAllByProductIdsIn(productIds: List<Long>): List<ProductDetail> {
		return productDetailJpaRepository.findAllByProductIdIn(productIds)
	}

	@Transactional(readOnly = true)
	override fun getByProductId(productId: Long): ProductDetail {
		return productDetailJpaRepository.findByProductId(productId)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_PRODUCT)
	}

	@Transactional
	override fun getByIdWithWriteLock(id: Long): ProductDetail {
		return productDetailJpaRepository.findByIdWithLock(id)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_PRODUCT)
	}

	override fun getAllByIdsInWithLock(ids: List<Long>): List<ProductDetail> {
		return productDetailJpaRepository.findAllByIdInWithLock(ids)
	}

	@Transactional
	override fun updateProductQuantityDecrease(id: Long, orderQuantity: Int): ProductDetail {
		val productDetail = (productDetailJpaRepository.findByIdWithLock(id)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_PRODUCT))

		productDetail.decreaseQuantity(orderQuantity)

		return productDetailJpaRepository.save(productDetail)
	}
}