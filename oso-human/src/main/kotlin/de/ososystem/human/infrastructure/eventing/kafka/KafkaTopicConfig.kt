package de.ososystem.human.infrastructure.eventing.kafka

import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaAdmin

@Configuration
class KafkaTopicConfig {
    @Bean
    fun kafkaAdmin(@Value(value = "\${kafka.bootstrapAddress}") bootstrapAddress: String): KafkaAdmin
        = KafkaAdmin(
            mapOf(
                AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapAddress
            )
        )

    @Bean
    fun topicHuman(
        @Value(value = "\${kafka.topic.human.name}") name: String,
        @Value(value = "\${kafka.topic.human.partitions}") partitions: Int,
        @Value(value = "\${kafka.topic.human.replications}") replicationFactor: Short
    ): NewTopic
        = NewTopic(name, partitions, replicationFactor)
}