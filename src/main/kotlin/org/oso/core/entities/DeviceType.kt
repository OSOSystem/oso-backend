package org.oso.core.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
data class DeviceType(
    @Id
    @Column(unique = true)
    @Size(min = 1)
    val name: String
) {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "deviceType")
    @JsonBackReference
    val devices = mutableSetOf<Device>()
}