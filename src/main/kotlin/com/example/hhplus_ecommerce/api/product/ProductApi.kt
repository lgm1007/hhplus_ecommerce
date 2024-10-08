package com.example.hhplus_ecommerce.api.product

import com.example.hhplus_ecommerce.api.product.response.ProductResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1")
@RestController
class ProductApi() {
	/**
	 * 상품 조회 API
	 */
	@GetMapping("/products")
	fun getProducts(): ResponseEntity<List<ProductResponse>> {
		return ResponseEntity.ok(
			listOf(
				ProductResponse(1L, "상품 A", 5000, 10),
				ProductResponse(2L, "상품 B", 3000, 5),
			)
		)
	}
}