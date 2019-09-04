package de.ososystem.device.domain.mapper

import de.ososystem.device.domain.entities.Human

interface HumanMapper {
    fun read(payload: ByteArray): Human
}