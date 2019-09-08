package de.ososystem.device.domain.factories

import de.ososystem.device.domain.comm.DeviceTypeCommStra
import de.ososystem.device.domain.entities.DeviceType

interface DeviceTypeCommStraFactory {
    fun getCommStraFor(type: DeviceType): DeviceTypeCommStra?
}