package com.example.hhplus_ecommerce.api.interceptor

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class ApiLoggingInterceptor : HandlerInterceptor {
	private val logger = KotlinLogging.logger {}

	override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
		when {
			Objects.equals(request.method, "POST") -> {
				val inputMap = ObjectMapper().readValue(request.inputStream, Map::class.java)

				logger.info("요청 URL: {}", request.requestURL)
				logger.info("요청 정보: {}", inputMap)
			}
			else -> {
				logger.info("요청 URL: {}", request.requestURL)
				logger.info("요청 정보: {}", request.queryString)
			}
		}
		return true
	}
}