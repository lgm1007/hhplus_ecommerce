package com.example.hhplus_ecommerce.application.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.scheduling.config.ScheduledTaskRegistrar

@Configuration
@EnableScheduling
class SchedulerConfig : SchedulingConfigurer {
	private val POOL_SIZE = 10

	override fun configureTasks(taskRegistrar: ScheduledTaskRegistrar) {
		taskRegistrar.setTaskScheduler(poolScheduler())
	}

	@Bean
	fun poolScheduler(): TaskScheduler {
		val scheduler = ThreadPoolTaskScheduler()
		scheduler.poolSize = POOL_SIZE
		scheduler.setThreadNamePrefix("scheduled-task-pool-")
		return scheduler
	}
}