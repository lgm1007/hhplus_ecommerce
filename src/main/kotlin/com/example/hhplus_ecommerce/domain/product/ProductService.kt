package com.example.hhplus_ecommerce.domain.product

import com.example.hhplus_ecommerce.domain.product.dto.ProductInfo
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
	private val productRepository: ProductRepository,
	private val productDetailRepository: ProductDetailRepository
) {
	@Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
	fun getProductListWithPaging(pageable: Pageable): List<ProductInfo> {
		val products = productRepository.getAllByPaging(pageable)
		val productIds = products.map { product -> product.id }
		val productDetails = productDetailRepository.getAllByProductIdsIn(productIds)

		return ProductInfo.listOf(products, productDetails)
	}

	@Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
	fun getProductById(productId: Long): ProductInfo {
		val product = productRepository.getById(productId)
		val productDetail = productDetailRepository.getByProductIdWithReadLock(productId)

		return ProductInfo.of(product, productDetail)
	}
}