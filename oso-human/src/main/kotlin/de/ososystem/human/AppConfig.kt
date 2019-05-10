package de.ososystem.human

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import de.ososystem.human.domain.entities.Human
import de.ososystem.human.domain.factories.EventFactory
import de.ososystem.human.domain.factories.HumanFactory
import de.ososystem.human.domain.factories.impl.EventFactoryImpl
import de.ososystem.human.domain.factories.impl.HumanFactoryImpl
import de.ososystem.human.domain.mapper.HumanMapper
import de.ososystem.human.domain.repositories.EventRepository
import de.ososystem.human.domain.repositories.HumanRepository
import de.ososystem.human.domain.services.EventService
import de.ososystem.human.domain.services.HumanService
import de.ososystem.human.domain.services.impl.HumanServiceImpl
import de.ososystem.human.infrastructure.repositories.EventRepositorySpring
import de.ososystem.human.infrastructure.repositories.HumanRepositorySpring
import de.ososystem.human.infrastructure.repositories.impl.EventRepositoryImpl
import de.ososystem.human.infrastructure.repositories.impl.HumanRepositoryImpl
import de.ososystem.human.infrastructure.services.EventServiceKafka
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType
import org.springframework.context.annotation.aspectj.EnableSpringConfigured
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.retry.backoff.FixedBackOffPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.client.RestTemplate


/**
 * Provides bean declarations for the spring application context.
 */
@Configuration
@EnableSpringConfigured
@ComponentScan(basePackages = [ "de.ososystem.human.infrastructure" ])
@EnableJpaRepositories(basePackages = [ "de.ososystem.human.infrastructure.repositories" ],
    excludeFilters = [ ComponentScan.Filter(type = FilterType.REGEX, pattern = [ ".*Impl.*" ]) ])
@EnableScheduling
@EnableAsync
class AppConfig (
    @Autowired
    val context: ApplicationContext
) {
    @Bean
    fun retryTemplate(): RetryTemplate {
        val retryPolicy = SimpleRetryPolicy(3)
        val backOffPolicy = FixedBackOffPolicy()
        backOffPolicy.backOffPeriod = 3000

        val retryTemplate = RetryTemplate()
        retryTemplate.setRetryPolicy(retryPolicy)
        retryTemplate.setBackOffPolicy(backOffPolicy)

        return retryTemplate
    }



    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper().registerKotlinModule()
    }

    @Bean
    fun humanMapper(): HumanMapper = object : HumanMapper {
        override fun read(array: ByteArray) =
            objectMapper().readValue(array, Human::class.java)

        override fun write(human: Human) =
            objectMapper().writeValueAsBytes(human)
    }



    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }



    @Bean
    fun humanFactory(humanRepository: HumanRepository): HumanFactory = HumanFactoryImpl(humanRepository)

    @Bean
    fun humanService(humanRepository: HumanRepository, humanFactory: HumanFactory, eventService: EventService): HumanService =
        HumanServiceImpl(humanFactory, humanRepository, eventService)

    @Bean
    fun humanRepository(humanRepositorySpring: HumanRepositorySpring): HumanRepository = HumanRepositoryImpl(humanRepositorySpring)

    @Bean
    fun eventService(eventFactory: EventFactory, eventRepository: EventRepository, eventRepositorySpring: EventRepositorySpring, kafkaTemplate: KafkaTemplate<String, String>, objectMapper: ObjectMapper): EventService
        = EventServiceKafka(eventFactory, eventRepository, eventRepositorySpring, kafkaTemplate, objectMapper)

    @Bean
    fun eventFactory(eventRepository: EventRepository): EventFactory = EventFactoryImpl(eventRepository, humanMapper())

    @Bean
    fun eventRepository(eventRepositorySpring: EventRepositorySpring): EventRepository = EventRepositoryImpl(eventRepositorySpring)
}