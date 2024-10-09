package com.example.hhplus_ecommerce.api.statistic

import com.example.hhplus_ecommerce.api.error.ErrorBody
import com.example.hhplus_ecommerce.api.error.ErrorStatus
import com.example.hhplus_ecommerce.api.statistic.response.ProductStatisticResponse
import com.example.hhplus_ecommerce.exception.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1")
@RestController
class StatisticApi {
	/**
	 * 상위 상품 조회 API
	 */
	@GetMapping("/statistics/products/top")
	fun topProductsStatistic(): ResponseEntity<Any> {
		try {
			return ResponseEntity.ok(
				listOf(
					ProductStatisticResponse(1L, "상품 A", 50),
					ProductStatisticResponse(2L, "상품 B", 40)
				)
			)
		} catch (e: NotFoundException) {
			return ResponseEntity(ErrorBody(ErrorStatus.NOT_FOUND_PRODUCT.message, 404), HttpStatus.NOT_FOUND)
		}
	}
}