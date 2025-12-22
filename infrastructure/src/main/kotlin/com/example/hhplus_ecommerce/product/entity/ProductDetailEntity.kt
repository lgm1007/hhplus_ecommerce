package com.example.hhplus_ecommerce.product.entity

import com.example.hhplus_ecommerce.product.ProductCategory
import com.example.hhplus_ecommerce.product.ProductDetail
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.Table

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "PRODUCTDETAIL", indexes = [Index(name = "idx_product_detail_product_id", columnList = "productId")])
class ProductDetailEntity(
	val productId: Long,
	val price: Int,
	var stockQuantity: Int,
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
		fun from(productDetail: ProductDetail): ProductDetailEntity {
			return ProductDetailEntity(
				productId = productDetail.productId,
				price = productDetail.price,
				stockQuantity = productDetail.stockQuantity,
				productCategory = productDetail.productCategory
			)
		}

		fun fromList(productDetails: List<ProductDetail>): List<ProductDetailEntity> {
			return productDetails.map(::from)
		}
	}

	fun toDomain(): ProductDetail {
		return ProductDetail(
			productDetailId = this.id,
			productId = this.productId,
			price = this.price,
			stockQuantity = this.stockQuantity,
			productCategory = this.productCategory,
			createdDate = this.createdDate,
			lastModifiedDate = this.lastModifiedDate
		)
	}
}