package com.example.hhplus_ecommerce.interceptor

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.util.ContentCachingRequestWrapper
import java.util.Objects
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class ApiLoggingInterceptor : HandlerInterceptor {
	private val logger = KotlinLogging.logger {}

	override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
		val wrappedRequest = if (request is ContentCachingRequestWrapper) request else ContentCachingRequestWrapper(request)

		when {
			Objects.equals(wrappedRequest.method, "POST") && wrappedRequest.contentType?.contains("application/json") == true -> {
				try {
					val body = String(wrappedRequest.contentAsByteArray, Charsets.UTF_8)
					val inputMap = ObjectMapper().readValue(body, Map::class.java)

					logger.info("요청 URL: {}", wrappedRequest.requestURL)
					logger.info("요청 정보: {}", inputMap)
				} catch (e: Exception) {
					logger.warn("요청 본문을 읽는 중 오류 발생: {}", e.message)
				}
			}
			else -> {
				logger.info("요청 URL: {}", wrappedRequest.requestURL)
				logger.info("요청 정보: {}", wrappedRequest.queryString)
			}
		}
		return true
	}
}