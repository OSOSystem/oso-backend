package de.ososystem.device.domain.services

import de.ososystem.device.domain.events.HumanEvent

interface EventService {
    fun processHumanEvent(event: HumanEvent)
}