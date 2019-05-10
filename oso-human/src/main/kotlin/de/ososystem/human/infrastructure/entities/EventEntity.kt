package de.ososystem.human.infrastructure.entities

import de.ososystem.human.domain.events.*
import java.time.ZonedDateTime
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity(name = "event")
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
    @Enumerated(value = EnumType.STRING)
    var sendState: SendState = SendState.UNSENT
    var lastSendTime: ZonedDateTime? = null
    var sendTries = 0

    fun toEvent() = HumanEvent(id, version, time, domain, type, key, payload)
}

enum class SendState {
    UNSENT,
    SENT,
    FAILED,
    SUCCESSFULL
}

fun HumanEvent.toEntity() = DomainEventEntity(id, version, time, domain, type, key, payload)