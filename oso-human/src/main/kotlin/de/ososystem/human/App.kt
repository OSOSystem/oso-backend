package de.ososystem.human

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.web.cors.CorsConfiguration

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@Configuration
class SecurityPermitAllConfig : WebSecurityConfigurerAdapter() {
    // TODO change this when the application gets deployed
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.cors().configurationSource { CorsConfiguration().applyPermitDefaultValues() }
        http.csrf().disable()
    }
}