package de.ososystem.human.domain.mapper

import de.ososystem.human.domain.entities.Human

interface HumanMapper {
    fun write(human: Human): ByteArray
    fun read(array: ByteArray): Human
}