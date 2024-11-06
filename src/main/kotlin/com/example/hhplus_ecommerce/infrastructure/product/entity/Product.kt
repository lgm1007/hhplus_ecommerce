package com.example.hhplus_ecommerce.infrastructure.product.entity

import com.example.hhplus_ecommerce.domain.product.dto.ProductDto
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
class Product(
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
		fun from(productDto: ProductDto): Product {
			return Product(
				productDto.name,
				productDto.description
			)
		}
	}
}