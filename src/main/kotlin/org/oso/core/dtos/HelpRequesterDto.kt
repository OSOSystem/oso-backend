package org.oso.core.dtos

import org.oso.core.entities.HelpRequester

data class HelpRequesterDto(
    var id: String = "DummyId",
    var name: String = ""
)

fun HelpRequester.toDto() = HelpRequesterDto(
    id = id!!,
    name = name
)