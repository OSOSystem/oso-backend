package de.ososystem.device.domain.services

import de.ososystem.device.domain.entities.DeviceType

interface DeviceTypeService {
    fun getAllDeviceTypes(): Iterable<DeviceType>
    fun getDeviceType(name: String): DeviceType?

    fun getPossibleDeviceTypes(payload: ByteArray): Set<DeviceType>
    fun receiveData(connId: String, payload: ByteArray)
}