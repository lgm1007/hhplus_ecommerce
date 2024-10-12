package com.example.hhplus_ecommerce.infrastructure.product

import com.example.hhplus_ecommerce.infrastructure.product.entity.ProductDetail
import org.springframework.data.jpa.repository.JpaRepository

interface ProductDetailJpaRepository : JpaRepository<ProductDetail, Long> {
	fun findByProductId(productId: Long): ProductDetail?
}