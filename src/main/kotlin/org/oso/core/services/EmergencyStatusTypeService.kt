package org.oso.core.services

import org.oso.core.entities.EmergencyStatusType

interface EmergencyStatusTypeService {
    fun findByName(name: String): EmergencyStatusType
}