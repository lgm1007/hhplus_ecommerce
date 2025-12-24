package com.example.hhplus_ecommerce.product.entity

import com.example.hhplus_ecommerce.BaseEntity
import com.example.hhplus_ecommerce.product.Product
import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "PRODUCT")
class ProductEntity(
	val name: String,
	val description: String,
) : Serializable, BaseEntity() {
	constructor(id: Long, name: String, description: String) : this(name, description) {
		this.id = id
	}

	companion object {
		fun from(product: Product): ProductEntity {
			return ProductEntity(
				name = product.name,
				description = product.description
			)
		}
	}

	fun toDomain(): Product {
		return Product(
			productId = this.id,
			name = this.name,
			description = this.description,
			createdDate = this.createdDate
		)
	}
}