package org.oso.filters

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import javax.servlet.*
import javax.servlet.http.HttpServletRequest

@Component
class HttpRequestLoggingFilter : Filter {
    override fun destroy() {
    }

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        if (request is HttpServletRequest) {
            val ipAddress = extractIp(request)
            val httpVerb = request.method

            LOGGER.info("Http {} Request {} {}", httpVerb, request.servletPath, ipAddress)
        }

        if (SecurityContextHolder.getContext().authentication is KeycloakAuthenticationToken) {
           with(SecurityContextHolder.getContext().authentication as KeycloakAuthenticationToken) {
               LOGGER.info("Request done by keycloak user ${this.name}")
           }
        } else {
            LOGGER.info("Request done by anonymous user")
        }

        chain?.doFilter(request, response)
    }

    override fun init(filterConfig: FilterConfig?) {
    }

    /**
     * Extracts the ip address of [request] considering the chance it was sent using a transparent proxy.
     * @param request The http request to extract the headers from.
     * @return The ip address of the client.
     */
    private fun extractIp(request: HttpServletRequest): String {
        val ipAddresses = request.getHeaders("X-Forwarded-For").toList()

        if (ipAddresses.isEmpty()) {
            return request.remoteAddr
        }

        return ipAddresses.first { ip -> ip != request.remoteAddr }
    }

    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(HttpRequestLoggingFilter::class.java)
    }
}