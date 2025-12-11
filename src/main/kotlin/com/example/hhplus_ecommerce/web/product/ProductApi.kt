package com.example.hhplus_ecommerce.web.product

import com.example.hhplus_ecommerce.domain.share.exception.ExceptionBody
import com.example.hhplus_ecommerce.web.product.response.ProductResponse
import com.example.hhplus_ecommerce.web.product.response.ProductResponseItem
import com.example.hhplus_ecommerce.domain.product.ProductService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "상품 API")
@RequestMapping("/api/v1")
@RestController
class ProductApi(private val productService: ProductService) {
	/**
	 * 상품 목록 조회 API
	 */
	@GetMapping("/products")
	@Operation(summary = "상품 목록 조회", description = "상품 목록을 조회한다.")
	@ApiResponses(value = [
		ApiResponse(responseCode = "200", description = "상품 조회 성공",
			content = [ Content(mediaType = "application/json", schema = Schema(implementation = ProductResponse::class)) ]),
		ApiResponse(responseCode = "404", description = "상품 없음",
			content = [ Content(mediaType = "application/json", schema = Schema(implementation = ExceptionBody::class)) ]),
	])
	fun getProducts(@PageableDefault(size = 10, page = 1) pageable: Pageable): ResponseEntity<ProductResponse> {
		return ResponseEntity.ok(
			ProductResponse.from(productService.getAllProductInfosWithPaging(pageable))
		)
	}

	/**
	 * 특정 상품 조회 API
	 */
	@GetMapping("/products/{productId}")
	@Operation(summary = "상품 조회", description = "특정 상품을 조회한다.")
	@ApiResponses(value = [
		ApiResponse(responseCode = "200", description = "상품 조회 성공",
			content = [ Content(mediaType = "application/json", schema = Schema(implementation = ProductResponseItem::class)) ]),
		ApiResponse(responseCode = "404", description = "상품 없음",
			content = [ Content(mediaType = "application/json", schema = Schema(implementation = ExceptionBody::class)) ]),
	])
	fun getProduct(@PathVariable productId: Long): ResponseEntity<ProductResponseItem> {
		return ResponseEntity.ok(
			ProductResponseItem.from(productService.getProductInfoById(productId))
		)
	}

}