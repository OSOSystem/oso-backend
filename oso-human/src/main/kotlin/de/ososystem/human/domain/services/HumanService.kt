package de.ososystem.human.domain.services

import de.ososystem.human.domain.dtos.HumanDto
import de.ososystem.human.domain.entities.Human
import java.util.*

val DOMAIN_HUMAN = "Human"

val TYPE_CREATED = "Created"
val TYPE_CHANGED = "Changed"
val TYPE_DELETED = "Deleted"

interface HumanService {
    fun deleteHuman(name: String)
    fun createHuman(humanDto: HumanDto): Human
    fun changeHuman(human: Human): Human

    fun getAllHumans(): Iterable<Human>
    fun getHuman(id: UUID): Human?
}