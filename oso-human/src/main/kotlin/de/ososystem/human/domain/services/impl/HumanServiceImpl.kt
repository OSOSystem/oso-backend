package de.ososystem.human.domain.services.impl

import de.ososystem.human.domain.dtos.HumanDto
import de.ososystem.human.domain.dtos.fromDto
import de.ososystem.human.domain.entities.Human
import de.ososystem.human.domain.exceptions.HumanAlreadyExistsException
import de.ososystem.human.domain.exceptions.HumanNotFoundException
import de.ososystem.human.domain.factories.HumanFactory
import de.ososystem.human.domain.repositories.HumanRepository
import de.ososystem.human.domain.services.*
import java.util.*

class HumanServiceImpl(
    val humanFactory: HumanFactory,
    val humanRepository: HumanRepository,
    val eventService: EventService
): HumanService {

    override fun changeHuman(human: Human): Human {
        val humanCheck = humanRepository.findHumanById(human.id) ?: throw HumanNotFoundException("Human<${human.id}> not found")

        return if(humanCheck != human) {
            humanRepository.saveHuman(human).also {
                eventService.fireHumanEvent(DOMAIN_HUMAN, TYPE_CHANGED, it)
            }
        } else human
    }

    override fun createHuman(humanDto: HumanDto): Human {
        humanRepository.findHumanByName(humanDto.name)?.let {
            throw HumanAlreadyExistsException("Human<${humanDto.name}> already exists")
        }

        val human = humanRepository.saveHuman(
                        humanFactory.createHuman(humanDto.name)
                        .fromDto(humanDto))

        eventService.fireHumanEvent(DOMAIN_HUMAN, TYPE_CREATED, human)

        return human
    }

    override fun deleteHuman(name: String) {
        humanRepository.findHumanByName(name)?.let {
            humanRepository.deleteHuman(it)

            eventService.fireHumanEvent(DOMAIN_HUMAN, TYPE_DELETED, it)
        }
    }

    override fun getAllHumans(): Iterable<Human> {
        return humanRepository.findAllHumans()
    }

    override fun getHuman(id: UUID): Human? {
        return humanRepository.findHumanById(id)
    }
}