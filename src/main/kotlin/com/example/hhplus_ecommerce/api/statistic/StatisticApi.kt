package com.example.hhplus_ecommerce.api.statistic

import com.example.hhplus_ecommerce.api.error.ErrorBody
import com.example.hhplus_ecommerce.api.statistic.response.ProductStatisticResponse
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

@Api(tags = ["통계 API"])
@RequestMapping("/api/v1")
@RestController
class StatisticApi {
	/**
	 * 상위 상품 조회 API
	 */
	@GetMapping("/statistics/products/top")
	@ApiOperation(value = "최근 3일간 가장 많이 팔린 상위 5개 상품 통계", notes = "최근 3일간 가장 많이 팔린 상위 5개 상품 정보를 조회한다.")
	@ApiResponses(value = [
		ApiResponse(code = 200, message = "OK", response = List::class),
		ApiResponse(code = 404, message = "상품 없음", response = ErrorBody::class),
	])
	fun topProductsStatistic(): ResponseEntity<Any> {
		try {
			return ResponseEntity.ok(
				listOf(
					ProductStatisticResponse(1L, "상품 A", 50),
					ProductStatisticResponse(2L, "상품 B", 40)
				)
			)
		} catch (e: NotFoundException) {
			return ResponseEntity(ErrorBody(e.errorStatus.message, 404), HttpStatus.NOT_FOUND)
		}
	}
}