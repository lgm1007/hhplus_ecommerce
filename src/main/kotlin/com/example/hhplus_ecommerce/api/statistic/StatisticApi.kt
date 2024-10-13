package com.example.hhplus_ecommerce.api.statistic

import com.example.hhplus_ecommerce.api.error.ErrorBody
import com.example.hhplus_ecommerce.api.statistic.response.ProductStatisticResponse
import com.example.hhplus_ecommerce.api.statistic.response.ProductStatisticResponseItem
import com.example.hhplus_ecommerce.exception.NotFoundException
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "통계 API")
@RequestMapping("/api/v1")
@RestController
class StatisticApi {
	/**
	 * 상위 상품 조회 API
	 */
	@GetMapping("/statistics/products/top")
	@Operation(summary = "최근 3일간 가장 많이 팔린 상위 5개 상품 통계", description = "최근 3일간 가장 많이 팔린 상위 5개 상품 정보를 조회한다.")
	@ApiResponses(value = [
		ApiResponse(responseCode = "200", description = "상위 상품 조회 성공",
			content = [ Content(mediaType = "application/json", schema = Schema(implementation = ProductStatisticResponse::class)) ]),
		ApiResponse(responseCode = "404", description = "상품 없음",
			content = [ Content(mediaType = "application/json", schema = Schema(implementation = ErrorBody::class)) ]),
	])
	fun topProductsStatistic(): ResponseEntity<Any> {
		try {
			return ResponseEntity.ok(
				ProductStatisticResponse(
					listOf(
						ProductStatisticResponseItem(1L, "상품 A", 50),
						ProductStatisticResponseItem(2L, "상품 B", 40)
					)
				)
			)
		} catch (e: NotFoundException) {
			return ResponseEntity(ErrorBody(e.errorStatus.message, 404), HttpStatus.NOT_FOUND)
		}
	}
}