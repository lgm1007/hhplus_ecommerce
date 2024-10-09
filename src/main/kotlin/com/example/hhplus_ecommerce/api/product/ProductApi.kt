package com.example.hhplus_ecommerce.api.product

import com.example.hhplus_ecommerce.api.error.ErrorBody
import com.example.hhplus_ecommerce.api.product.response.ProductResponse
import com.example.hhplus_ecommerce.exception.NotFoundException
import org.springframework.http.HttpStatus
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
	fun getProducts(): ResponseEntity<Any> {
		try {
			return ResponseEntity.ok(
				listOf(
					ProductResponse(1L, "상품 A", 5000, 10),
					ProductResponse(2L, "상품 B", 3000, 5),
				)
			)
		} catch (e: NotFoundException) {
			return ResponseEntity(ErrorBody(e.errorStatus.message, 404), HttpStatus.NOT_FOUND)
		}
	}
}