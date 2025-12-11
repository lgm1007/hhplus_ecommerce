package com.example.hhplus_ecommerce.infrastructure.product

import com.example.hhplus_ecommerce.domain.share.exception.ErrorStatus
import com.example.hhplus_ecommerce.domain.product.ProductDetailRepository
import com.example.hhplus_ecommerce.domain.product.dto.ProductDetailDto
import com.example.hhplus_ecommerce.domain.share.exception.NotFoundException
import com.example.hhplus_ecommerce.infrastructure.product.entity.ProductDetailEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class ProductDetailRepositoryImpl(
	private val productDetailJpaRepository: ProductDetailJpaRepository
) : ProductDetailRepository {
	override fun insert(productDetailDto: ProductDetailDto): ProductDetailEntity {
		return productDetailJpaRepository.save(ProductDetailEntity.from(productDetailDto))
	}

	override fun getAllByProductIdsIn(productIds: List<Long>): List<ProductDetailEntity> {
		return productDetailJpaRepository.findAllByProductIdIn(productIds)
	}

	@Transactional(readOnly = true)
	override fun getByProductId(productId: Long): ProductDetailEntity {
		return productDetailJpaRepository.findByProductId(productId)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_PRODUCT)
	}

	@Transactional
	override fun getByIdWithWriteLock(id: Long): ProductDetailEntity {
		return productDetailJpaRepository.findByIdWithLock(id)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_PRODUCT)
	}

	override fun getAllByIdsInWithLock(ids: List<Long>): List<ProductDetailEntity> {
		return productDetailJpaRepository.findAllByIdInWithLock(ids)
	}

	override fun getAllByIdsIn(ids: List<Long>): List<ProductDetailEntity> {
		return productDetailJpaRepository.findAllByIdIn(ids)
	}

	@Transactional
	override fun updateProductQuantityDecreaseWithLock(id: Long, orderQuantity: Int): ProductDetailEntity {
		val productDetail = (productDetailJpaRepository.findByIdWithLock(id)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_PRODUCT))

		productDetail.decreaseQuantity(orderQuantity)

		return productDetailJpaRepository.save(productDetail)
	}

	@Transactional
	override fun updateProductQuantityDecrease(id: Long, orderQuantity: Int): ProductDetailEntity {
		val productDetail = (productDetailJpaRepository.findByIdOrNull(id)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_PRODUCT))

		productDetail.decreaseQuantity(orderQuantity)

		return productDetailJpaRepository.save(productDetail)
	}
}