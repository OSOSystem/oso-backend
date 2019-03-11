package org.oso.core.repositories

import org.oso.core.entities.EmergencyStatus
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EmergencyStatusRepository : CrudRepository<EmergencyStatus, Long>