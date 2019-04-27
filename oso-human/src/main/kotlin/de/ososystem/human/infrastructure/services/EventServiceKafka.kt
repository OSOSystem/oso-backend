package de.ososystem.human.infrastructure.services

import de.ososystem.human.domain.events.HumanEvent
import de.ososystem.human.domain.factories.EventFactory
import de.ososystem.human.domain.repositories.EventRepository
import de.ososystem.human.domain.services.impl.EventServiceBase

class EventServiceKafka(
    eventFactory: EventFactory,
    eventRepository: EventRepository
) : EventServiceBase(
    eventFactory
) {
    override fun fireEvent(event: HumanEvent) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}