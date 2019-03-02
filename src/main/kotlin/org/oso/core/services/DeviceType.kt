package org.oso.core.services

import org.oso.core.entities.DeviceType

interface DeviceTypeService {
    fun findAll(): List<DeviceType>
}