package org.oso.human

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.aspectj.EnableSpringConfigured
import org.springframework.retry.backoff.FixedBackOffPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate
import org.springframework.web.client.RestTemplate


/**
 * Provides bean declarations for the spring application context.
 */
@Configuration
@EnableSpringConfigured
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
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}