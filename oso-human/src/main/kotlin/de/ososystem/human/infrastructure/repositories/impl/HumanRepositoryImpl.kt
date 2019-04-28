package de.ososystem.human.infrastructure.repositories.impl

import de.ososystem.human.domain.entities.Human
import de.ososystem.human.domain.repositories.HumanRepository
import de.ososystem.human.infrastructure.entities.HumanEntity
import de.ososystem.human.infrastructure.entities.toEntity
import de.ososystem.human.infrastructure.repositories.HumanRepositorySpring
import java.util.*

class HumanRepositoryImpl(
    val humanRepositorySpring: HumanRepositorySpring
) : HumanRepository, HumanRepositorySpring by humanRepositorySpring {

    override fun saveHuman(human: Human): Human = save(human.toEntity()).toHuman()
    override fun saveAllHumans(humans: Iterable<Human>): Iterable<Human> = saveAll(humans.toEntities()).toHumans()

    override fun findHumanById(id: UUID): Human? = findById(id).orElse(null)?.toHuman()
    override fun findHumanByName(name: String): Human? = findByName(name).orElse(null)?.toHuman()
    override fun findAllHumans(): Iterable<Human> = findAll().toHumans()
    override fun humanExistsById(id: UUID): Boolean = existsById(id)
    override fun findAllHumansById(ids: Iterable<UUID>): Iterable<Human> = findAllById(ids).toHumans()

    override fun countHumans(): Long = count()

    override fun deleteHuman(human: Human) = delete(human.toEntity())
    override fun deleteHumanById(id: UUID) = deleteById(id)
    override fun deleteAllHumans(humans: Iterable<Human>) = deleteAll(humans.toEntities())
    override fun deleteAllHumans() = deleteAll()
}


private fun Iterable<Human>.toEntities() = this.map { it.toEntity() }
private fun Iterable<HumanEntity>.toHumans() = this.map { it.toHuman() }