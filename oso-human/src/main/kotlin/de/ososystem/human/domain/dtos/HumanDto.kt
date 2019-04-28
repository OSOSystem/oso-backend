package de.ososystem.human.domain.dtos

import de.ososystem.human.domain.entities.Human

data class HumanDto(var name: String, var keycloakName: String?) {
}

fun Human.toDto() = HumanDto(name, keycloakName)
fun Human.fromDto(dto: HumanDto) = this.also {
    name = dto.name
    keycloakName = dto.keycloakName
}