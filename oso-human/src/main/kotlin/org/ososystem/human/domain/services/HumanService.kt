package org.ososystem.human.domain.services

import org.ososystem.human.domain.dtos.HumanDto
import org.ososystem.human.domain.entities.Human

interface HumanService {
    fun deleteHuman(name: String)
    fun createHuman(humanDto: HumanDto)
    fun changeHuman(human: Human)
}