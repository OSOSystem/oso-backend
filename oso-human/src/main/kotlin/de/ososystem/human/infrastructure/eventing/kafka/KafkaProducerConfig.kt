package de.ososystem.human.infrastructure.eventing.kafka

import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import java.util.HashMap
import org.springframework.kafka.core.ProducerFactory



@Configuration
class KafkaProducerConfig {
    @Bean
    fun producerFactory(
        @Value(value = "\${kafka.bootstrapAddress}") bootstrapAddress: String
    ): ProducerFactory<String, String>
        = DefaultKafkaProducerFactory(
            mapOf(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapAddress,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java
            )
    )

    @Bean
    fun kafkaTemplate(producerFactory: ProducerFactory<String, String>): KafkaTemplate<String, String>
        = KafkaTemplate(producerFactory)
}