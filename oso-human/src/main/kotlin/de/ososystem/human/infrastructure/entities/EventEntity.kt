package de.ososystem.human.infrastructure.entities

import de.ososystem.human.domain.events.*
import java.time.ZonedDateTime
import javax.persistence.Entity

@Entity
class DomainEventEntity(
    @javax.persistence.Id
    val id: Id,
    val version: Version,
    val time: ZonedDateTime,

    val domain: Domain,
    val type: Type,
    val key: String,

    val payload: ByteArray
) {
    fun toEvent() = HumanEvent(id, version, time, domain, type, key, payload)
}

fun HumanEvent.toEntity() = DomainEventEntity(id, version, time, domain, type, key, payload)