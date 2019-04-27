package de.ososystem.human.domain.services

import de.ososystem.human.domain.entities.Human
import de.ososystem.human.domain.events.Domain
import de.ososystem.human.domain.events.Type

interface EventService {
    fun fireHumanEvent(domain: Domain, type: Type, human: Human)
}