package com.example.hhplus_ecommerce.product

import com.example.hhplus_ecommerce.product.entity.ProductDetailEntity
import com.example.hhplus_ecommerce.share.exception.ErrorStatus
import com.example.hhplus_ecommerce.share.exception.NotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class ProductDetailRepositoryImpl(
	private val productDetailJpaRepository: ProductDetailJpaRepository
) : ProductDetailRepository {
	override fun insert(productDetail: ProductDetail): ProductDetail {
		return productDetailJpaRepository.save(ProductDetailEntity.from(productDetail))
			.toDomain()
	}

	override fun getAllByProductIdsIn(productIds: List<Long>): List<ProductDetail> {
		val findEntities = productDetailJpaRepository.findAllByProductIdIn(productIds)
		return findEntities.map { it.toDomain() }
	}

	@Transactional(readOnly = true)
	override fun getByProductId(productId: Long): ProductDetail {
		val findEntity =  productDetailJpaRepository.findByProductId(productId)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_PRODUCT)

		return findEntity.toDomain()
	}

	@Transactional
	override fun getByIdWithWriteLock(id: Long): ProductDetail {
		val findEntity =  productDetailJpaRepository.findByIdWithLock(id)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_PRODUCT)

		return findEntity.toDomain()
	}

	override fun getAllByIdsInWithLock(ids: List<Long>): List<ProductDetail> {
		val findEntities = productDetailJpaRepository.findAllByIdInWithLock(ids)
		return findEntities.map { it.toDomain() }
	}

	override fun getAllByIdsIn(ids: List<Long>): List<ProductDetail> {
		val findEntities = productDetailJpaRepository.findAllByIdIn(ids)
		return findEntities.map { it.toDomain() }
	}

	@Transactional
	override fun updateProductQuantityDecreaseWithLock(id: Long, orderQuantity: Int): ProductDetail {
		val findEntity = (productDetailJpaRepository.findByIdWithLock(id)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_PRODUCT))
		val productDetail = findEntity.toDomain()

		productDetail.decreaseQuantity(orderQuantity)
		findEntity.stockQuantity = productDetail.stockQuantity

		return productDetail
	}

	@Transactional
	override fun updateProductQuantityDecrease(id: Long, orderQuantity: Int): ProductDetail {
		val findEntity = (productDetailJpaRepository.findByIdOrNull(id)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_PRODUCT))
		val productDetail = findEntity.toDomain()

		productDetail.decreaseQuantity(orderQuantity)
		findEntity.stockQuantity = productDetail.stockQuantity

		return productDetail
	}
}