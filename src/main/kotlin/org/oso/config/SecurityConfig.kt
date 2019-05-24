//package org.oso.config
//
//import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver
//import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents
//import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.ComponentScan
//import org.springframework.context.annotation.Configuration
//import org.springframework.context.annotation.FilterType
//import org.springframework.core.annotation.Order
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
//import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper
//import org.springframework.security.core.session.SessionRegistryImpl
//import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy
//import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy
//
//// because of a bug in spring-keycloak-adapter the KeycloakConfiguration-annotation leeds to a httpSessionManager-Bean
//// being added to spring twice and so to an exception
//// a workaround is to instead add the following 3 annotations and exlude the 2nd HttpSessionManager
////@KeycloakConfiguration
//@Configuration
//@ComponentScan(
//    basePackageClasses = [ KeycloakSecurityComponents::class],
//    excludeFilters = [
//        ComponentScan.Filter(
//            type = FilterType.REGEX,
//            pattern = [ "org.keycloak.adapters.springsecurity.management.HttpSessionManager" ]
//        )
//    ]
//)
//@EnableWebSecurity
//// Spring generates a webadapter with order 100 by default
//// so we need to assign any other number to avoid a conflict/exception
//@Order(99)
//internal class SecurityConfig : KeycloakWebSecurityConfigurerAdapter() {
//
//    @Autowired
//    @Throws(Exception::class)
//    fun configureGlobal(auth: AuthenticationManagerBuilder) {
//        val keycloakAuthenticationProvider = keycloakAuthenticationProvider()
//        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(SimpleAuthorityMapper())
//        auth.authenticationProvider(keycloakAuthenticationProvider)
//    }
//
//    @Bean
//    fun KeycloakConfigResolver(): KeycloakSpringBootConfigResolver {
//        return KeycloakSpringBootConfigResolver()
//    }
//
//    @Bean
//    override fun sessionAuthenticationStrategy(): SessionAuthenticationStrategy {
//        return RegisterSessionAuthenticationStrategy(SessionRegistryImpl())
//    }
//
//    @Throws(Exception::class)
//    override fun configure(http: HttpSecurity) {
//        super.configure(http)
//        http.authorizeRequests()
//            .antMatchers("/**")
//            .hasRole("user")
//            .anyRequest()
//            .permitAll()
//    }
//}