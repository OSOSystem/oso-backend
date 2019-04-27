package de.ososystem.human.domain.services

import de.ososystem.human.domain.dtos.HumanDto
import de.ososystem.human.domain.entities.Human

val DOMAIN_HUMAN = "Human"

val TYPE_CREATED = "Created"
val TYPE_CHANGED = "Changed"
val TYPE_DELETED = "Deleted"

interface HumanService {
    fun deleteHuman(name: String)
    fun createHuman(humanDto: HumanDto)
    fun changeHuman(human: Human)
}