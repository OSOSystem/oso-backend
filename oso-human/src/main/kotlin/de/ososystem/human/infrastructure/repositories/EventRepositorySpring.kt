package de.ososystem.human.infrastructure.repositories

import de.ososystem.human.infrastructure.entities.DomainEventEntity
import de.ososystem.human.infrastructure.entities.SendState
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface EventRepositorySpring: CrudRepository<DomainEventEntity, Long> {
    fun findFirstByOrderByIdDesc(): Optional<DomainEventEntity>
    fun findBySendStateNotIn(states: Collection<SendState>): Iterable<DomainEventEntity>
}