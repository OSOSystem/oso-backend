package org.oso.core.entities

import java.time.LocalDateTime
import javax.persistence.*

// Mark as unnecessary
@Entity
data class DeviceCoordinates(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id")
    val device: Device,
    @Column
    val time: LocalDateTime,
    @Embedded
    val coordinates: Coordinates
) : BaseEntity()