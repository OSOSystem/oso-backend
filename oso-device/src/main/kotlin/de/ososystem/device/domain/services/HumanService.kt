package de.ososystem.device.domain.services

import de.ososystem.device.domain.entities.Human

interface HumanService {
    fun createHuman(key: String, data: Human): Human
    fun deleteHuman(key: String)
    fun changeHuman(key: String, data: Human): Human
}