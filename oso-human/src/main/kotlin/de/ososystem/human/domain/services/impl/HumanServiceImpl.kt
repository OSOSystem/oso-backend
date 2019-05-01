package de.ososystem.human.domain.services.impl

import de.ososystem.human.domain.dtos.HumanDto
import de.ososystem.human.domain.dtos.fromDto
import de.ososystem.human.domain.entities.Human
import de.ososystem.human.domain.exceptions.HumanAlreadyExistsException
import de.ososystem.human.domain.exceptions.HumanNotFoundException
import de.ososystem.human.domain.factories.HumanFactory
import de.ososystem.human.domain.repositories.HumanRepository
import de.ososystem.human.domain.services.*
import org.slf4j.LoggerFactory
import java.util.*

class HumanServiceImpl(
    val humanFactory: HumanFactory,
    val humanRepository: HumanRepository,
    val eventService: EventService
): HumanService {

    override fun changeHuman(human: Human): Human {
        LOGGER.info("Changing Human requested with data<$human>")

        val humanCheck = humanRepository.findHumanById(human.id) ?: throw HumanNotFoundException("Human<${human.id}> not found")

        return if(humanCheck != human) {
            LOGGER.debug("human will actually be changed")
            humanRepository.saveHuman(human).also {
                LOGGER.debug("saving successful - sending change event")
                eventService.fireHumanEvent(DOMAIN_HUMAN, TYPE_CHANGED, it)
            }
        } else human
    }

    override fun createHuman(humanDto: HumanDto): Human {
        LOGGER.info("Creation of human requested using data<$humanDto>")
        humanRepository.findHumanByName(humanDto.name)?.let {
            throw HumanAlreadyExistsException("Human<${humanDto.name}> already exists")
        }

        val human = humanRepository.saveHuman(
                        humanFactory.createHuman(humanDto.name)
                        .fromDto(humanDto))

        LOGGER.debug("human created successful - sending created event with data<$human>")
        eventService.fireHumanEvent(DOMAIN_HUMAN, TYPE_CREATED, human)

        return human
    }

    override fun deleteHuman(name: String) {
        LOGGER.info("Deletion of human<$name> requested")
        humanRepository.findHumanByName(name)?.let {
            LOGGER.debug("human actually exists and will be deleted")
            humanRepository.deleteHuman(it)

            LOGGER.debug("Human deleted successful - sending deletion event")
            eventService.fireHumanEvent(DOMAIN_HUMAN, TYPE_DELETED, it)
        }
    }

    override fun getAllHumans(): Iterable<Human> {
        LOGGER.info("All humans requested")
        return humanRepository.findAllHumans()
    }

    override fun getHuman(id: UUID): Human? {
        LOGGER.info("Human<$id> requested")
        return humanRepository.findHumanById(id)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(HumanServiceImpl::class.java)
    }
}