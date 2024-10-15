package com.example.hhplus_ecommerce.infrastructure.product

import com.example.hhplus_ecommerce.api.error.ErrorStatus
import com.example.hhplus_ecommerce.domain.product.ProductRepository
import com.example.hhplus_ecommerce.domain.product.dto.ProductDto
import com.example.hhplus_ecommerce.exception.NotFoundException
import com.example.hhplus_ecommerce.infrastructure.product.entity.Product
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl(
	val productJpaRepository: ProductJpaRepository
) : ProductRepository {
	override fun insert(productDto: ProductDto): ProductDto {
		return ProductDto.from(productJpaRepository.save(Product.from(productDto)))
	}

	override fun getAllByPaging(pageable: Pageable): List<ProductDto> {
		return productJpaRepository.findAllWithPaging(pageable)
			.map(ProductDto.Companion::from)
	}

	override fun getById(id: Long): ProductDto {
		val product = productJpaRepository.findByIdOrNull(id)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_PRODUCT)

		return ProductDto.from(product)
	}

	override fun getAllByIds(ids: List<Long>): List<Product> {
		return productJpaRepository.findAllByIdIn(ids)
	}
}