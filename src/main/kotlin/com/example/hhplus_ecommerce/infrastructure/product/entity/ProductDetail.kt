package com.example.hhplus_ecommerce.infrastructure.product.entity

import com.example.hhplus_ecommerce.domain.product.ProductCategory
import com.example.hhplus_ecommerce.domain.product.dto.ProductDetailDto
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
class ProductDetail(
	val productId: Long,
	val stockQuantity: Int,
	val productCategory: ProductCategory,
) {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0

	@CreatedDate
	var createdDate: LocalDateTime = LocalDateTime.now()
		private set

	@LastModifiedDate
	var lastModifiedDate: LocalDateTime = LocalDateTime.now()
		private set

	companion object {
		fun from(productDetailDto: ProductDetailDto): ProductDetail {
			return ProductDetail(
				productDetailDto.productId,
				productDetailDto.stockQuantity,
				productDetailDto.productCategory
			)
		}
	}
}