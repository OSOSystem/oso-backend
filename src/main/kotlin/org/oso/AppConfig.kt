package org.oso

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.context.annotation.aspectj.EnableSpringConfigured
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.integration.channel.DirectChannel
import org.springframework.integration.dsl.IntegrationFlows
import org.springframework.integration.ip.tcp.TcpReceivingChannelAdapter
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.messaging.MessageChannel
import org.springframework.retry.backoff.FixedBackOffPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate
import org.springframework.web.client.RestTemplate
import java.nio.charset.StandardCharsets
import javax.sql.DataSource


/**
 * Provides bean declarations for the spring application context.
 */
@Configuration
@EnableSpringConfigured
class AppConfig (
    @Autowired
    val context: ApplicationContext
) {
    /**
     * Provides an in-memory database for the testing environment when starting integration tests.
     */
    @Bean
    @Profile("test")
    fun dataSource(): DataSource {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName("org.h2.Driver")
        dataSource.url = "jdbc:h2:mem:db;DB_CLOSE_DELAY=-1"
        dataSource.username = ""
        dataSource.password = ""
        return dataSource
    }

    /**
     * This bean purpose is solely for the testing environment.
     * The testing environment can't create the bean because of the missing configuration keys.
     * In the production environment they are defined in the Dockerfile.
     */
    @Bean
    @Profile("test")
    fun javaMailSender(): JavaMailSender {
        return JavaMailSenderImpl()
    }

    @Bean(name = ["messageSource"])
    fun reloadableResourceBundleMessageSource(): ReloadableResourceBundleMessageSource {
        return ReloadableResourceBundleMessageSource().apply {
            setBasenames("classpath:messages")
            setDefaultEncoding("UTF-8")
        }
    }

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

    @Bean
    fun cf(@Value("\${tcp.server.port}") tcpPort: Int): AbstractServerConnectionFactory {
        return TcpNetServerConnectionFactory(tcpPort)
    }

    @Bean
    fun inbound(cf: AbstractServerConnectionFactory): TcpReceivingChannelAdapter {
        val adapter = TcpReceivingChannelAdapter()
        adapter.setConnectionFactory(cf)
        adapter.setOutputChannel(tcpIn())
        return adapter
    }

    @Bean
    fun tcpIn(): MessageChannel = DirectChannel()

    @Bean
    fun inputFlow() =
        IntegrationFlows.from(tcpIn())
            .route(IDENTIFY_BEAN, "route")
            .get()

    @Bean
    fun unknownFlow() =
        IntegrationFlows.from(UNKNOWN_CHANNEL)
            .transform(bean(IDENTIFY_BEAN), "concat")
            .route(IDENTIFY_BEAN, "identify")
            .get()

    @Bean
    fun realUnknownFlow() =
        IntegrationFlows.from(REAL_UNKNOWN_CHANNEL)
            .handle(IDENTIFY_BEAN, "savePayload")
            .get()

    @Bean
    fun reachfarFlow() =
        IntegrationFlows.from(REACHFAR_CHANNEL)
            .filter<ByteArray> { !it.isEmpty() }
            .transform<ByteArray, String> { String(it, 0, it.size, StandardCharsets.US_ASCII) }
            .handle(REACHFAR_BEAN, "handle")
            .get()

    private fun bean(beanName: String) = context.getBean(beanName)

    companion object {
        const val UNKNOWN_CHANNEL = "unknownChannel"
        const val REAL_UNKNOWN_CHANNEL = "realUnknownChannel"
        const val REACHFAR_CHANNEL = "reachfarChannel"

        const val IDENTIFY_BEAN = "identify"
        const val REACHFAR_BEAN = "reachfar"
    }
}