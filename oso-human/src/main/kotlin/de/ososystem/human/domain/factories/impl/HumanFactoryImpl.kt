package de.ososystem.human.domain.factories.impl

import de.ososystem.human.domain.entities.Human
import de.ososystem.human.domain.factories.HumanFactory
import de.ososystem.human.domain.repositories.HumanRepository
import java.util.*

class HumanFactoryImpl(
    val humanRepository: HumanRepository
) : HumanFactory {
    override fun createHuman(name: String): Human {
        return Human(nextId(), name)
    }

    private fun nextId() = UUID.randomUUID()
}