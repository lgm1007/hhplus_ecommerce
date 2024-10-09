package com.example.hhplus_ecommerce.api.product

import com.example.hhplus_ecommerce.api.error.ErrorBody
import com.example.hhplus_ecommerce.api.product.response.ProductResponse
import com.example.hhplus_ecommerce.exception.NotFoundException
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Api(tags = ["상품 API"])
@RequestMapping("/api/v1")
@RestController
class ProductApi() {
	/**
	 * 상품 조회 API
	 */
	@GetMapping("/products")
	@ApiOperation(value = "상품 조회", notes = "상품 목록을 조회한다.")
	@ApiResponses(value = [
		ApiResponse(code = 200, message = "OK", response = List::class),
		ApiResponse(code = 404, message = "상품 없음", response = ErrorBody::class),
	])
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