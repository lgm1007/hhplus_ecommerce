package com.example.hhplus_ecommerce.statistics

import com.example.hhplus_ecommerce.share.exception.ExceptionBody
import com.example.hhplus_ecommerce.statistics.dto.OrderProductStatisticsResponseDto
import com.example.hhplus_ecommerce.statistics.response.OrderProductStatisticsResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "통계 API")
@RequestMapping("/api/v1")
@RestController
class StatisticApi(private val statisticsFacade: StatisticsFacade) {
	/**
	 * 상위 상품 조회 API
	 */
	@GetMapping("/statistics/products/top")
	@Operation(summary = "최근 3일간 가장 많이 팔린 상위 5개 상품 통계", description = "최근 3일간 가장 많이 팔린 상위 5개 상품 정보를 조회한다.")
	@ApiResponses(value = [
		ApiResponse(responseCode = "200", description = "상위 상품 조회 성공",
			content = [ Content(mediaType = "application/json", schema = Schema(implementation = OrderProductStatisticsResponseDto::class)) ]),
		ApiResponse(responseCode = "404", description = "상품 없음",
			content = [ Content(mediaType = "application/json", schema = Schema(implementation = ExceptionBody::class)) ]),
	])
	fun topProductsStatistic(): ResponseEntity<OrderProductStatisticsResponse> {
		return ResponseEntity.ok(
			OrderProductStatisticsResponse(
				// 최근 3일 간 가장 많이 주문한 상품 상위 5개
				statisticsFacade.getTopOrderProductStatistics(3, 5)
			)
		)
	}
}