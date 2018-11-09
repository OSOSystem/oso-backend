package org.oso.core.entities

import javax.persistence.*

@Entity
data class Device(
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "helpRequester_id")
    var helpRequester: HelpRequester? = null,
    var name: String,
    var description: String? = null,
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "deviceType_id")
    var deviceType: DeviceType? = null
) : BaseEntity() {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "device")
    val deviceCoordinates = mutableSetOf<DeviceCoordinates>()
}

