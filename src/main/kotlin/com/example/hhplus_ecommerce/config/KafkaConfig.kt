package com.example.hhplus_ecommerce.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import org.springframework.kafka.support.serializer.JsonDeserializer

@EnableKafka
@Configuration
class KafkaConfig {
	@Value("\${spring.kafka.producer.bootstrap-servers}")
	lateinit var producerBootstrapServers: String

	@Value("\${spring.kafka.producer.acks}")
	lateinit var acks: String

	@Value("\${spring.kafka.producer.key-serializer}")
	lateinit var keySerializer: String

	@Value("\${spring.kafka.producer.value-serializer}")
	lateinit var valueSerializer: String

	@Value("\${spring.kafka.consumer.bootstrap-servers}")
	lateinit var consumerBootstrapServers: String

	@Value("\${spring.kafka.consumer.key-deserializer}")
	lateinit var keyDeserializer: String

	@Value("\${spring.kafka.consumer.value-deserializer}")
	lateinit var valueDeserializer: String

	@Value("\${spring.kafka.consumer.group-id}")
	lateinit var groupId: String

	@Value("\${spring.kafka.listener.concurrency}")
	lateinit var concurrency: String

	@Value("\${spring.kafka.listener.poll-timeout}")
	lateinit var pollTimeout: String

	@Bean
	fun producerProps(): Map<String, Any> {
		val props = hashMapOf<String, Any>()
		props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = producerBootstrapServers
		props[ProducerConfig.ACKS_CONFIG] = acks
		props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = keySerializer
		props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = valueSerializer
		return props
	}

	@Bean
	fun producerFactory(): ProducerFactory<String, Any> {
		return DefaultKafkaProducerFactory<String, Any>(producerProps())
	}

	@Bean
	fun kafkaTemplate(): KafkaTemplate<String, Any> {
		return KafkaTemplate<String, Any>(producerFactory())
	}

	@Bean
	fun consumerProps(): Map<String, Any> {
		val props = hashMapOf<String, Any>()
		props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = consumerBootstrapServers
		props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = keyDeserializer
		props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = valueDeserializer
		props[ConsumerConfig.GROUP_ID_CONFIG] = groupId
		props[JsonDeserializer.TRUSTED_PACKAGES] = "*"
		return props
	}

	@Bean
	fun consumerFactory(): ConsumerFactory<Int, Any> {
		return DefaultKafkaConsumerFactory<Int, Any>(consumerProps())
	}

	@Bean
	fun kafkaListenerContainerFactory(): KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Int, Any>> {
		val factory = ConcurrentKafkaListenerContainerFactory<Int, Any>()
		factory.consumerFactory = consumerFactory()
		factory.setConcurrency(concurrency.toInt())
		factory.containerProperties.pollTimeout = pollTimeout.toLong()
		return factory
	}
}