package org.oso.core.services

import org.oso.core.entities.Coordinates
import org.oso.core.entities.Device
import org.oso.core.entities.DeviceType
import java.time.LocalDateTime

interface DeviceService {
    fun findAll(): List<Device>
    fun createIfMissing(name: String, description: String, deviceType: DeviceType? = null): Device
    fun findTypeByName(name: String): DeviceType?
    fun saveCoordinates(device: Device, coordinates: Coordinates, now: LocalDateTime)

    companion object {
        const val DEVICE_TYPE_REACHFAR = "Reachfar"
        const val DEVICE_TYPE_NANO_TRACKER = "Nano Tracker"
        const val DEVICE_TYPE_FLIC = "Flic"
        const val DEVICE_TYPE_APP = "App"
    }
}