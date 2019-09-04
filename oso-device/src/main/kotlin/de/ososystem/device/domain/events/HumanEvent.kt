package de.ososystem.device.domain.events

import java.time.ZonedDateTime

val DOMAIN_HUMAN = "Human"

val TYPE_CREATED = "Created"
val TYPE_CHANGED = "Changed"
val TYPE_DELETED = "Deleted"

class HumanEvent(
        override val id: Id,
        override val version: Version,
        override val time: ZonedDateTime,

        override val domain: Domain,
        override val type: Type,
        override val key: String,

        override val payload: ByteArray
): Event