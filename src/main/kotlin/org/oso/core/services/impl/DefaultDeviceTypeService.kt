package org.oso.core.services.impl

import org.oso.core.entities.DeviceType
import org.oso.core.repositories.DeviceTypeRepository
import org.oso.core.services.DeviceTypeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DefaultDeviceTypeService
@Autowired constructor(private val deviceTypeRepository: DeviceTypeRepository): DeviceTypeService {

    override fun findAll(): List<DeviceType> {
        return deviceTypeRepository.findAll().toList()
    }
}