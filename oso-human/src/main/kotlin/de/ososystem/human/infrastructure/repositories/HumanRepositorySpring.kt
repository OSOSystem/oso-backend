package de.ososystem.human.infrastructure.repositories

import de.ososystem.human.infrastructure.entities.HumanEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface HumanRepositorySpring: CrudRepository<HumanEntity, UUID> {
    fun findByName(name: String): Optional<HumanEntity>
}