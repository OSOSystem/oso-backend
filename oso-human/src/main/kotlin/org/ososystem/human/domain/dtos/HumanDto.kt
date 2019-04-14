package org.ososystem.human.domain.dtos

import org.ososystem.human.domain.entities.Human

data class HumanDto(val name: String, var keycloakName: String?) {
}

fun Human.toDto() = HumanDto(name, keycloakName)
fun Human.fromDto(dto: HumanDto) = this.also {
    name = dto.name
    keycloakName = dto.keycloakName
}