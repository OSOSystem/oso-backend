package org.oso.core.repositories

import org.oso.core.entities.EmergencyActionType
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EmergencyActionTypeRepository : CrudRepository<EmergencyActionType, String>