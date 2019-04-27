package de.ososystem.human.domain.factories

import de.ososystem.human.domain.entities.Human
import de.ososystem.human.domain.events.Domain
import de.ososystem.human.domain.events.HumanEvent
import de.ososystem.human.domain.events.Type

interface EventFactory {
    fun createHumanEvent(domain: Domain, type: Type, human: Human): HumanEvent
}