package de.ososystem.human.infrastructure.repositories

import de.ososystem.human.domain.entities.Human
import de.ososystem.human.domain.repositories.HumanRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface HumanRepositorySpring: HumanRepository, CrudRepository<Human, UUID> {
    override fun saveHuman(human: Human): Human = save(human)
    override fun saveAllHumans(humans: Iterable<Human>): Iterable<Human> = saveAll(humans)

    override fun findHumanById(id: UUID): Human? = findById(id).orElse(null)
    override fun findHumanByName(name: String): Human? = findByName(name).orElse(null)
    fun findByName(name: String): Optional<Human>
    override fun findAllHumans(): Iterable<Human> = findAll()
    override fun humanExistsById(id: UUID): Boolean = existsById(id)
    override fun findAllHumansById(ids: Iterable<UUID>): Iterable<Human> = findAllById(ids)

    override fun countHumans(): Long = count()

    override fun deleteHuman(human: Human) = delete(human)
    override fun deleteHumanById(id: UUID) = deleteById(id)
    override fun deleteAllHumans(humans: Iterable<Human>) = deleteAll(humans)
    override fun deleteAllHumans() = deleteAll()
}