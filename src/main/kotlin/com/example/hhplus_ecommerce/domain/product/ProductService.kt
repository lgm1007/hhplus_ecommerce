package com.example.hhplus_ecommerce.domain.product

import com.example.hhplus_ecommerce.api.error.ErrorStatus
import com.example.hhplus_ecommerce.domain.product.dto.ProductDetailDto
import com.example.hhplus_ecommerce.domain.product.dto.ProductDto
import com.example.hhplus_ecommerce.domain.product.dto.ProductInfo
import com.example.hhplus_ecommerce.domain.product.dto.ProductStatisticsInfo
import com.example.hhplus_ecommerce.exception.NotFoundException
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
	private val productRepository: ProductRepository,
	private val productDetailRepository: ProductDetailRepository
) {
	@Transactional(readOnly = true)
	fun getAllProductInfosWithPaging(pageable: Pageable): List<ProductInfo> {
		val products = productRepository.getAllByPaging(pageable).map(ProductDto.Companion::from)
		val productIds = products.map { product -> product.id }
		val productDetails = productDetailRepository.getAllByProductIdsIn(productIds).map(ProductDetailDto.Companion::from)

		val productInfos = ProductInfo.listOf(products, productDetails)

		if (productInfos.isEmpty()) {
			throw NotFoundException(ErrorStatus.NOT_FOUND_PRODUCT)
		}

		return productInfos
	}

	@Transactional(readOnly = true)
	fun getProductInfoById(productId: Long): ProductInfo {
		val product = ProductDto.from(productRepository.getById(productId))
		val productDetail = ProductDetailDto.from(productDetailRepository.getByProductId(productId))

		return ProductInfo.of(product, productDetail)
	}

	@Transactional(readOnly = true)
	fun getAllProductDetailsByDetailIdsInWithLock(productDetailIds: List<Long>): List<ProductDetailDto> {
		return ProductDetailDto.fromList(productDetailRepository.getAllByIdsInWithLock(productDetailIds))
	}

	@Transactional(readOnly = true)
	fun getAllProductStatisticsInfos(productDetailIds: List<Long>): List<ProductStatisticsInfo> {
		val productDetailDtos = ProductDetailDto.fromList(productDetailRepository.getAllByIdsIn(productDetailIds))
		val productIds = productDetailDtos.map { it.productId }

		val productDtos = ProductDto.fromList(productRepository.getAllByIds(productIds))

		return ProductStatisticsInfo.listOf(productDtos, productDetailDtos)
	}

	@Transactional
	fun updateProductQuantityDecrease(productDetailId: Long, orderQuantity: Int): ProductDetailDto {
		return ProductDetailDto.from(productDetailRepository.updateProductQuantityDecrease(productDetailId, orderQuantity))
	}
}