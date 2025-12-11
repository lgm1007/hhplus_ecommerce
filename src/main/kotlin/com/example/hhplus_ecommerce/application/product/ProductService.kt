package com.example.hhplus_ecommerce.application.product

import com.example.hhplus_ecommerce.domain.product.ProductDetailRepository
import com.example.hhplus_ecommerce.domain.product.ProductRepository
import com.example.hhplus_ecommerce.domain.product.dto.ProductDetailDto
import com.example.hhplus_ecommerce.domain.product.dto.ProductDto
import com.example.hhplus_ecommerce.domain.product.dto.ProductInfo
import com.example.hhplus_ecommerce.domain.product.dto.ProductStatisticsInfo
import com.example.hhplus_ecommerce.domain.share.exception.ErrorStatus
import com.example.hhplus_ecommerce.domain.share.exception.NotFoundException
import com.example.hhplus_ecommerce.infrastructure.lock.RedisLockSupporter
import org.redisson.api.RedissonClient
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.LockSupport

@Service
class ProductService(
	private val productRepository: ProductRepository,
	private val productDetailRepository: ProductDetailRepository,
	private val redisLockSupporter: RedisLockSupporter,
	private val redissonClient: RedissonClient
) {
	@Transactional(readOnly = true)
	fun getAllProductInfosWithPaging(pageable: Pageable): List<ProductInfo> {
		val products = productRepository.getAllByPaging(pageable).map(ProductDto.Companion::from)
		val productIds = products.map { product -> product.id }
		val productDetails = productDetailRepository.getAllByProductIdsIn(productIds).map(ProductDetailDto.Companion::from)

		val productInfos = ProductInfo.Companion.listOf(products, productDetails)

		if (productInfos.isEmpty()) {
			throw NotFoundException(ErrorStatus.NOT_FOUND_PRODUCT)
		}

		return productInfos
	}

	@Transactional(readOnly = true)
	fun getProductInfoById(productId: Long): ProductInfo {
		val product = ProductDto.Companion.from(productRepository.getById(productId))
		val productDetail = ProductDetailDto.Companion.from(productDetailRepository.getByProductId(productId))

		return ProductInfo.Companion.of(product, productDetail)
	}

	@Transactional(readOnly = true)
	fun getAllProductDetailsByDetailIdsInWithLock(productDetailIds: List<Long>): List<ProductDetailDto> {
		return ProductDetailDto.Companion.fromList(productDetailRepository.getAllByIdsInWithLock(productDetailIds))
	}

	@Transactional(readOnly = true)
	fun getAllProductDetailsByIdsIn(productDetailIds: List<Long>): List<ProductDetailDto> {
		return ProductDetailDto.Companion.fromList(productDetailRepository.getAllByIdsIn(productDetailIds))
	}

	@Transactional(readOnly = true)
	fun getAllProductStatisticsInfos(productDetailIds: List<Long>): List<ProductStatisticsInfo> {
		val productDetailDtos = ProductDetailDto.Companion.fromList(productDetailRepository.getAllByIdsIn(productDetailIds))

		if (productDetailDtos.isEmpty()) {
			throw NotFoundException(ErrorStatus.NOT_FOUND_PRODUCT)
		}

		val productIds = productDetailDtos.map { it.productId }

		val productDtos = ProductDto.Companion.fromList(productRepository.getAllByIds(productIds))

		return ProductStatisticsInfo.Companion.listOf(productDtos, productDetailDtos)
	}

	@Transactional
	fun updateProductQuantityDecreaseWithDBLock(productDetailId: Long, orderQuantity: Int): ProductDetailDto {
		return ProductDetailDto.Companion.from(productDetailRepository.updateProductQuantityDecreaseWithLock(productDetailId, orderQuantity))
	}

	fun updateProductQuantityDecreaseWithLettuce(productDetailId: Long, orderQuantity: Int): ProductDetailDto {
		val lockKey = "product:${productDetailId}"
		while (!redisLockSupporter.lock(lockKey)) {
			LockSupport.parkNanos(10_000_000)   // 10 ms, 10 * 1_000_000 ns
		}

		try {
			return ProductDetailDto.Companion.from(
				productDetailRepository.updateProductQuantityDecrease(
					productDetailId, orderQuantity
				)
			)
		} finally {
			redisLockSupporter.unlock(lockKey)
		}
	}

	fun updateProductQuantityDecreaseWithRedisson(productDetailId: Long, orderQuantity: Int): ProductDetailDto {
		val rLock = redisLockSupporter.getRLock("product:${productDetailId}")

		try {
			val acquireLock = rLock.tryLock(10, 1, TimeUnit.SECONDS)
			if (!acquireLock) {
				throw InterruptedException("Lock 획득 실패")
			}
			return ProductDetailDto.Companion.from(
				productDetailRepository.updateProductQuantityDecrease(productDetailId, orderQuantity)
			)
		} finally {
			rLock.unlock()
		}
	}

	@Transactional
	fun updateProductQuantityDecrease(productDetailId: Long, orderQuantity: Int) {
		productDetailRepository.updateProductQuantityDecrease(
			productDetailId, orderQuantity
		)
	}
}