package org.oso.core.dtos

import org.oso.core.entities.HelpRequester

data class HelpRequesterDto(
    var id: Long = 0,
    var name: String = "",
    var email: String? = null,
    var phoneNumber: String? = null,
    var password: String? = null
)

fun HelpRequester.toDto() = HelpRequesterDto(
    id = id!!,
    name = name,
    email = email,
    phoneNumber = phoneNumber,
    password = password
)