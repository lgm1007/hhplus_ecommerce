package com.example.hhplus_ecommerce.product.entity

import com.example.hhplus_ecommerce.product.Product
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "PRODUCT")
class ProductEntity(
	val name: String,
	val description: String,
) : Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long = 0
		private set

	@CreatedDate
	var createdDate: LocalDateTime = LocalDateTime.now()
		private set

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