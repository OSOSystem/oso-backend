package de.ososystem.device.domain.entities

class DeviceType(
        val name: String
) {
    val trigger = mutableListOf<DeviceTrigger>()
}