package de.ososystem.human.infrastructure.repositories.impl

import de.ososystem.human.domain.events.HumanEvent
import de.ososystem.human.domain.events.Id
import de.ososystem.human.domain.repositories.EventRepository
import de.ososystem.human.infrastructure.entities.toEntity
import de.ososystem.human.infrastructure.repositories.EventRepositorySpring

class EventRepositoryImpl(
    val eventRepositorySpring: EventRepositorySpring
) : EventRepository, EventRepositorySpring by eventRepositorySpring {

    override fun deleteEvent(event: HumanEvent) = delete(event.toEntity())
    override fun findEventWithHighestId() = findFirstByOrderByIdDesc().orElse(null)?.toEvent()
    override fun saveEvent(event: HumanEvent) = save(event.toEntity()).toEvent()
    override fun findEventById(id: Id): HumanEvent? = findById(id).map { it.toEvent() }.orElse(null)
}