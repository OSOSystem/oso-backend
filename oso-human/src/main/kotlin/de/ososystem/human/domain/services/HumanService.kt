package de.ososystem.human.domain.services

import de.ososystem.human.domain.dtos.HumanDto
import de.ososystem.human.domain.entities.Human

interface HumanService {
    fun deleteHuman(name: String)
    fun createHuman(humanDto: HumanDto)
    fun changeHuman(human: Human)
}