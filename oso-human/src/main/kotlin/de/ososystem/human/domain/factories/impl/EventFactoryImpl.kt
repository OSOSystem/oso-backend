package de.ososystem.human.domain.factories.impl

import de.ososystem.human.domain.entities.Human
import de.ososystem.human.domain.events.Domain
import de.ososystem.human.domain.events.HumanEvent
import de.ososystem.human.domain.events.Id
import de.ososystem.human.domain.events.Type
import de.ososystem.human.domain.factories.EventFactory
import de.ososystem.human.domain.mapper.HumanMapper
import de.ososystem.human.domain.repositories.EventRepository
import java.time.ZonedDateTime

class EventFactoryImpl(
    val eventRepository: EventRepository,
    val humanMapper: HumanMapper
) : EventFactory {

    override fun createHumanEvent(domain: Domain, type: Type, human: Human): HumanEvent {
        return HumanEvent(nextId(), "1.0", ZonedDateTime.now(), domain, type, human.id.toString(), humanMapper.write(human))
    }

    private fun nextId(): Id {
        return (eventRepository.findEventWithHighestId()?.id ?: 0) + 1
    }
}