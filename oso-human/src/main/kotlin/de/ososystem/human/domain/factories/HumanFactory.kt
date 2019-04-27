package de.ososystem.human.domain.factories

import de.ososystem.human.domain.entities.Human

interface HumanFactory {
    fun createHuman(name: String): Human
}