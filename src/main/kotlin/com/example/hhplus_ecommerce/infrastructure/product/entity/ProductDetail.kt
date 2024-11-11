package com.example.hhplus_ecommerce.infrastructure.product.entity

import com.example.hhplus_ecommerce.api.error.ErrorStatus
import com.example.hhplus_ecommerce.domain.product.ProductCategory
import com.example.hhplus_ecommerce.domain.product.dto.ProductDetailDto
import com.example.hhplus_ecommerce.exception.BadRequestException
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "PRODUCTDETAIL", indexes = [Index(name = "idx_product_detail_product_id", columnList = "productId")])
class ProductDetail(
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

	fun decreaseQuantity(orderQuantity: Int) {
		if (this.stockQuantity < orderQuantity) {
			throw BadRequestException(ErrorStatus.NOT_ENOUGH_QUANTITY)
		}
		this.stockQuantity -= orderQuantity
	}

	companion object {
		fun from(productDetailDto: ProductDetailDto): ProductDetail {
			return ProductDetail(
				productDetailDto.productId,
				productDetailDto.price,
				productDetailDto.stockQuantity,
				productDetailDto.productCategory
			)
		}

		fun fromList(productDetailDtos: List<ProductDetailDto>): List<ProductDetail> {
			return productDetailDtos.map(::from)
		}
	}
}