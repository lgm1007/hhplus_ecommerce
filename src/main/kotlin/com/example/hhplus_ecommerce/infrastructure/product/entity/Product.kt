package com.example.hhplus_ecommerce.infrastructure.product.entity

import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Product(
	val name: String,
	val price: Int,
	val description: String,
	@CreatedDate
	val createdDate: LocalDateTime
) {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0
}