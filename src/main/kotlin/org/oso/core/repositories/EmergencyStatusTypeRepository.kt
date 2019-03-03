package org.oso.core.repositories

import org.oso.core.entities.EmergencyStatusType
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EmergencyStatusTypeRepository : CrudRepository<EmergencyStatusType, String>