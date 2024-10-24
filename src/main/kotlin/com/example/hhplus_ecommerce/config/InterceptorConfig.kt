package com.example.hhplus_ecommerce.config

import com.example.hhplus_ecommerce.api.interceptor.ApiLoggingInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class InterceptorConfig(
	private val apiLoggingInterceptor: ApiLoggingInterceptor
) : WebMvcConfigurer {
	override fun addInterceptors(registry: InterceptorRegistry) {
		registry.addInterceptor(apiLoggingInterceptor)
			.addPathPatterns("/api/v1/**")
	}
}