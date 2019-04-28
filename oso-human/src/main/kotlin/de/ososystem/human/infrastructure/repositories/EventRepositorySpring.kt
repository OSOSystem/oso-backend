package de.ososystem.human.infrastructure.repositories

import de.ososystem.human.infrastructure.entities.DomainEventEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface EventRepositorySpring: CrudRepository<DomainEventEntity, Long> {
    fun findFirstByOrderByIdDesc(): Optional<DomainEventEntity>
}