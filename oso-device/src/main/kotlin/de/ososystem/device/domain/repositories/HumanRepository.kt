package de.ososystem.device.domain.repositories

import de.ososystem.device.domain.entities.Human
import java.util.*

interface HumanRepository {
    fun findHumanById(id: UUID): Human?
    fun humanExistsById(id: UUID): Boolean = findHumanById(id) != null

    fun saveHuman(human: Human): Human
    fun deleteHumanById(id: UUID)
}