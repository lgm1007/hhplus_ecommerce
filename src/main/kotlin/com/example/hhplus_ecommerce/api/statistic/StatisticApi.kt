package com.example.hhplus_ecommerce.api.statistic

import com.example.hhplus_ecommerce.api.statistic.response.ProductStatisticResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1")
@RestController
class StatisticApi {
	@GetMapping("/statistic/products/top")
	fun topProductsStatistic(): ResponseEntity<List<ProductStatisticResponse>> {
		return ResponseEntity.ok(
			listOf(
				ProductStatisticResponse(1L, "상품 A", 50),
				ProductStatisticResponse(2L, "상품 B", 40)
			)
		)
	}
}