package com.example.hhplus_ecommerce

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableAsync

@EnableJpaAuditing
@EnableAsync
@SpringBootApplication
class HhplusEcommerceApplication

fun main(args: Array<String>) {
	runApplication<HhplusEcommerceApplication>(*args)
}
