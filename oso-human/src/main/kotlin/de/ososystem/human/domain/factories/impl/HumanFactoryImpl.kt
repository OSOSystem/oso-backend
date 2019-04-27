package de.ososystem.human.domain.factories.impl

import de.ososystem.human.domain.entities.Human
import de.ososystem.human.domain.factories.HumanFactory
import java.util.*

class HumanFactoryImpl: HumanFactory {
    override fun createHuman(name: String): Human {
        return Human(UUID.randomUUID(), name)
    }
}