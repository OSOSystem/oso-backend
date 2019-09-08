package de.ososystem.device.domain.comm

import de.ososystem.device.domain.entities.Device
import de.ososystem.device.domain.entities.DeviceType

interface DeviceTypeCommStra {
    fun isValidMsg(payload: ByteArray): Boolean
    fun receiveData(deviceType: DeviceType, device: Device?, connId: String, payload: ByteArray)
}