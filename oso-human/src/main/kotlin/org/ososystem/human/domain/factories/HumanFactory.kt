package org.ososystem.human.domain.factories

import org.ososystem.human.domain.entities.Human

interface HumanFactory {
    fun createHuman(name: String): Human
}