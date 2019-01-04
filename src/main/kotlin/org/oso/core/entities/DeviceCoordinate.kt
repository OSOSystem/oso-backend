package org.oso.core.entities

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@IdClass(DeviceCoordinateId::class)
data class DeviceCoordinate(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id")
    @Id
    val device: Device,
    @Column
    @Id
    val time: LocalDateTime,
    @Embedded
    val coordinates: Coordinate
)

data class DeviceCoordinateId(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id")
    @Id
    val device: Device,
    @Column
    @Id
    val time: LocalDateTime
) : Serializable