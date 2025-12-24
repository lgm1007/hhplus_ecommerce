package com.example.hhplus_ecommerce

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

@EnableAsync
@EnableScheduling
@SpringBootApplication
class HhplusEcommerceApplication

fun main(args: Array<String>) {
	runApplication<HhplusEcommerceApplication>(*args)
}
