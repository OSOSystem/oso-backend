package org.oso.core.dtos

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.oso.core.entities.HelpProvider
import org.springframework.security.core.context.SecurityContextHolder

data class HelpProviderDto(
    var id: String,
    var name: String,
    var expoPushToken: String?
)

data class HelpProviderPushDto(
    var name: String
)

fun HelpProvider.toDto() = HelpProviderDto(
    id = id!!,
    name = name,
    expoPushToken = expoPushToken
)
