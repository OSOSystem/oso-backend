package org.oso.core.repositories

import org.oso.core.entities.EmergencyAction
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EmergencyActionRepository : CrudRepository<EmergencyAction, Long>