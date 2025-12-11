package com.example.hhplus_ecommerce.infrastructure.product

import com.example.hhplus_ecommerce.domain.share.exception.ErrorStatus
import com.example.hhplus_ecommerce.domain.product.ProductRepository
import com.example.hhplus_ecommerce.domain.product.dto.ProductDto
import com.example.hhplus_ecommerce.domain.share.exception.NotFoundException
import com.example.hhplus_ecommerce.infrastructure.product.entity.ProductEntity
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl(
	val productJpaRepository: ProductJpaRepository
) : ProductRepository {
	override fun insert(productDto: ProductDto): ProductEntity {
		return productJpaRepository.save(ProductEntity.from(productDto))
	}

	override fun getAllByPaging(pageable: Pageable): List<ProductEntity> {
		return productJpaRepository.findAllWithPaging(pageable)
	}

	@Cacheable(value = ["productInfo"], key = "#id.toString()", cacheManager = "redisCacheManager")
	override fun getById(id: Long): ProductEntity {
		return productJpaRepository.findByIdOrNull(id)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_PRODUCT)
	}

	override fun getAllByIds(ids: List<Long>): List<ProductEntity> {
		return productJpaRepository.findAllByIdIn(ids)
	}
}