package org.oso.core.repositories

import org.oso.core.entities.DeviceType
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DeviceTypeRepository : CrudRepository<DeviceType, Long> {
    fun findByName(name: String): DeviceType?
}