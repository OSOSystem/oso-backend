package de.ososystem.human.infrastructure.repositories

import de.ososystem.human.domain.events.HumanEvent
import de.ososystem.human.domain.repositories.EventRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface EventRepositorySpring: EventRepository, CrudRepository<HumanEvent, Long> {
    fun findFirstOrderByIdDesc(): Optional<HumanEvent>

    override fun deleteEvent(event: HumanEvent) = delete(event)
    override fun findEventWithHighestId() = findFirstOrderByIdDesc().orElse(null)
    override fun saveEvent(event: HumanEvent) = save(event)
}