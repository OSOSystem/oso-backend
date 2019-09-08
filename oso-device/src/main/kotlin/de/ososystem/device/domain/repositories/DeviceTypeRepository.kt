package de.ososystem.device.domain.repositories

import de.ososystem.device.domain.entities.DeviceType

interface DeviceTypeRepository {
    fun getAllDeviceTypes(): Iterable<DeviceType>

    fun findDeviceTypeByName(name: String): DeviceType?
}