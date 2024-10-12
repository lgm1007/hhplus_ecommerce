package com.example.hhplus_ecommerce.infrastructure.product

import com.example.hhplus_ecommerce.infrastructure.product.entity.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductJpaRepository : JpaRepository<Product, Long> {
}