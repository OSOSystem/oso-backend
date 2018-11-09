package org.oso.core.repositories

import org.oso.core.entities.Device
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DeviceRepository : CrudRepository<Device, Long> {
    fun findByName(name: String): Device?
}