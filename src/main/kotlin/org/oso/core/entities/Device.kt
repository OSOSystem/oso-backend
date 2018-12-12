package org.oso.core.entities

import javax.persistence.*
import javax.validation.constraints.Size

@Entity
data class Device(
    @Id
    @Column(nullable = false)
    @Size(min = 1)
    var id: String? = null,
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "helpRequester_id")
    var helpRequester: HelpRequester? = null,
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "deviceType_id")
    var deviceType: DeviceType? = null
) {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "device")
    val deviceCoordinates = mutableSetOf<DeviceCoordinate>()
}

