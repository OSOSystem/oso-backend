package de.ososystem.human.domain.repositories

import de.ososystem.human.domain.events.HumanEvent
import de.ososystem.human.domain.events.Id

interface EventRepository {
    fun saveEvent(event: HumanEvent): HumanEvent
    fun deleteEvent(event: HumanEvent)
    fun findEventWithHighestId(): HumanEvent?
    fun findEventById(id: Id): HumanEvent?
}