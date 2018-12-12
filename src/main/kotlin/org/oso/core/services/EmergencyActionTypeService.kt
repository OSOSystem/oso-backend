package org.oso.core.services

import org.oso.core.entities.EmergencyActionType

interface EmergencyActionTypeService {
    fun findByName(name: String): EmergencyActionType
}