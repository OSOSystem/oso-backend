package org.oso.core.dtos

import org.oso.core.entities.HelpProvider

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
