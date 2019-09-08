package de.ososystem.device.domain.services

import de.ososystem.device.domain.entities.Device

interface DeviceService {
    fun findDeviceByConnId(connId: String): Device?
}