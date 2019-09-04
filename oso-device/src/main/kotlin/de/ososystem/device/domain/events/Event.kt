package de.ososystem.device.domain.events

import java.time.ZonedDateTime

typealias Domain = String
typealias Version = String
typealias Type = String
typealias Id = Long

interface Event {
    val id: Id
    val version: Version
    val time: ZonedDateTime

    val domain: Domain
    val type: Type
    val key: String

    val payload: ByteArray
}