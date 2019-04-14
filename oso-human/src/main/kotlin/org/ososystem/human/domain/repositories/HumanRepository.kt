package org.ososystem.human.domain.repositories

import org.ososystem.human.domain.entities.Human
import java.util.*

interface HumanRepository {
    fun saveHuman(human: Human): Human
    fun saveAllHumans(humans: Iterable<Human>): Iterable<Human>

    fun findHumanById(id: UUID): Human?
    fun findHumanByName(name: String): Human?
    fun findAllHumans(): Iterable<Human>
    fun humanExistsById(id: UUID): Boolean
    fun findAllHumansById(ids: Iterable<UUID>): Iterable<Human>

    fun countHumans(): Long

    fun deleteHuman(human: Human)
    fun deleteHumanById(id: UUID)
    fun deleteAllHumans(humans: Iterable<Human>)
    fun deleteAllHumans()
}