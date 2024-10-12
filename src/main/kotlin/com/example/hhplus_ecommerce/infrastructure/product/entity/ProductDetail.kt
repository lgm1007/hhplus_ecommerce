package com.example.hhplus_ecommerce.infrastructure.product.entity

import com.example.hhplus_ecommerce.domain.product.ProductCategory
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class ProductDetail(
	val productId: Long,
	val stockQuantity: Int,
	val productCategory: ProductCategory,
	@CreatedDate
	val createdDate: LocalDateTime,
	@LastModifiedDate
	val lastModifiedDate: LocalDateTime
) {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0
}