package com.example.hhplus_ecommerce.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
	@Bean
	fun openAPI(): OpenAPI {
		val info = Info()
			.version("1.0.0")
			.title("hhplus ecommerce")
			.description("항해플러스 이커머스")
		return OpenAPI().info(info)
	}
}