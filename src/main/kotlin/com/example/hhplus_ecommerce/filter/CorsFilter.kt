package com.example.hhplus_ecommerce.filter

import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class CorsFilter : Filter {
	override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
		val httpRequest = request as HttpServletRequest
		val httpResponse = response as HttpServletResponse

		httpResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:3000")
		httpResponse.setHeader("Access-Control-Allow-Credentials", "true")
		httpResponse.setHeader("Access-Control-Allow-Methods","*")
		httpResponse.setHeader("Access-Control-Max-Age", "3600")
		httpResponse.setHeader("Access-Control-Allow-Headers",
			"Origin, X-Requested-With, Content-Type, Accept, Authorization")

		if ("OPTIONS" == httpRequest.method) {
			httpResponse.status = HttpServletResponse.SC_OK
		} else {
			chain?.doFilter(request, response)
		}
	}
}