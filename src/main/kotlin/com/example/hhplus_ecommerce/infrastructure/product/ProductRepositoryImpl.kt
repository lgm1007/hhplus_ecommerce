package com.example.hhplus_ecommerce.infrastructure.product

import com.example.hhplus_ecommerce.api.error.ErrorStatus
import com.example.hhplus_ecommerce.domain.product.ProductRepository
import com.example.hhplus_ecommerce.domain.product.dto.ProductDto
import com.example.hhplus_ecommerce.exception.NotFoundException
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl(
	val productJpaRepository: ProductJpaRepository
) : ProductRepository {
	override fun getAllByPaging(pageable: Pageable): List<ProductDto> {
		return productJpaRepository.findAllWithPaging(pageable)
			.map(ProductDto.Companion::from)
	}

	override fun getById(id: Long): ProductDto {
		val product = productJpaRepository.findByIdOrNull(id)
			?: throw NotFoundException(ErrorStatus.NOT_FOUND_PRODUCT)

		return ProductDto.from(product)
	}
}