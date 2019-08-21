package de.ososystem.device

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType
import org.springframework.context.annotation.aspectj.EnableSpringConfigured
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@EnableSpringConfigured
@ComponentScan(basePackages = [ "de.ososystem.device.infrastructure" ])
@EnableJpaRepositories(basePackages = [ "de.ososystem.device.infrastructure.repositories" ],
        excludeFilters = [ ComponentScan.Filter(type = FilterType.REGEX, pattern = [ ".*Impl.*" ]) ])
@EnableScheduling
@EnableAsync
class AppConfig {
}