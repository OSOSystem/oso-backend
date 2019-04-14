package org.ososystem.human.domain.services.impl

import org.ososystem.human.domain.dtos.HumanDto
import org.ososystem.human.domain.dtos.fromDto
import org.ososystem.human.domain.entities.Human
import org.ososystem.human.domain.exceptions.HumanAlreadyExistsException
import org.ososystem.human.domain.exceptions.HumanNotFoundException
import org.ososystem.human.domain.factories.HumanFactory
import org.ososystem.human.domain.repositories.HumanRepository
import org.ososystem.human.domain.services.EventService
import org.ososystem.human.domain.services.HumanService

class HumanServiceImpl(
    val humanFactory: HumanFactory,
    val humanRepository: HumanRepository,
    val eventService: EventService
): HumanService {

    override fun changeHuman(human: Human) {
        val humanCheck = humanRepository.findHumanById(human.id)

        humanCheck ?: throw HumanNotFoundException("Human<${human.id}> not found")

        if(humanCheck != human) {
            humanRepository.saveHuman(human)

            TODO("THROW EVENT")
        }
    }

    override fun createHuman(humanDto: HumanDto) {
        humanRepository.findHumanByName(humanDto.name)?.let {
            throw HumanAlreadyExistsException("Human<${humanDto.name}> already exists")
        }

        val human = humanRepository.saveHuman(
                        humanFactory.createHuman(humanDto.name)
                        .fromDto(humanDto))

        TODO("THROW EVENT")
    }

    override fun deleteHuman(name: String) {
        humanRepository.findHumanByName(name)?.let {
            humanRepository.deleteHuman(it)

            TODO("THROW EVENT")
        }
    }
}