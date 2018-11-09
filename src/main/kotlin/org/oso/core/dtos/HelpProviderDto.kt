package org.oso.core.dtos

import org.oso.core.entities.HelpProvider

data class HelpProviderDto(
    var id: Long,
    var name: String,
    var password: String,
    var email: String?,
    var expoPushToken: String?,
    var phoneNumber: String?
)

data class HelpProviderPushDto(
    var name: String,
    var password: String,
    var email: String? = null,
    var expoPushToken: String? = null,
    var phoneNumber: String? = null
) {
    fun toEntity() = HelpProvider(
        name = name,
        password =  password,
        email = email,
        expoPushToken = expoPushToken,
        phoneNumber = phoneNumber
    )
}

fun HelpProvider.toDto() = HelpProviderDto(
    id = id!!,
    name = name,
    password = password,
    expoPushToken = expoPushToken,
    phoneNumber = phoneNumber,
    email = email
)
