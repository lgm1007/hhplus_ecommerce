package com.example.hhplus_ecommerce.product

import com.example.hhplus_ecommerce.product.entity.ProductEntity
import com.example.hhplus_ecommerce.share.exception.ErrorStatus
import com.example.hhplus_ecommerce.share.exception.NotFoundException
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl(
	val productJpaRepository: ProductJpaRepository
) : ProductRepository {
	override fun insert(product: Product): Product {
		return productJpaRepository.save(ProductEntity.from(product))
			.toDomain()
	}

	override fun getAllByPaging(page: Int, itemSize: Int): List<Product> {
		return productJpaRepository.findAllWithPaging(PageRequest.of(page, itemSize))
			.map { it.toDomain() }
	}

	@Cacheable(value = ["productInfo"], key = "#id.toString()", cacheManager = "redisCacheManager")
	override fun getById(id: Long): Product {
		val findEntity = productJpaRepository.findByIdOrNull(id)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_PRODUCT)

		return findEntity.toDomain()
	}

	override fun getAllByIds(ids: List<Long>): List<Product> {
		return productJpaRepository.findAllByIdIn(ids)
			.map { it.toDomain() }
	}
}