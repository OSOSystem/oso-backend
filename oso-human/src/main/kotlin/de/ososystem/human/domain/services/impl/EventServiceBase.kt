package de.ososystem.human.domain.services.impl

import de.ososystem.human.domain.entities.Human
import de.ososystem.human.domain.events.Domain
import de.ososystem.human.domain.events.HumanEvent
import de.ososystem.human.domain.events.Type
import de.ososystem.human.domain.factories.EventFactory
import de.ososystem.human.domain.repositories.EventRepository
import de.ososystem.human.domain.services.EventService
import org.slf4j.LoggerFactory

abstract class EventServiceBase(
    val eventFactory: EventFactory
) : EventService {

    override fun fireHumanEvent(domain: Domain, type: Type, human: Human) {
        LOGGER.info("firing event in domain<$domain> of type<$type> for human<$human>")
        fireEvent(
            eventFactory.createHumanEvent(domain, type, human)
        )
    }

    protected abstract fun fireEvent(event: HumanEvent)

    companion object {
        private val LOGGER = LoggerFactory.getLogger(EventServiceBase::class.java)
    }
}