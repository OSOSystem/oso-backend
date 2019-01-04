package org.oso.core.repositories

import org.oso.core.entities.DeviceCoordinate
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DeviceCoordinatesRepository : CrudRepository<DeviceCoordinate, Long>