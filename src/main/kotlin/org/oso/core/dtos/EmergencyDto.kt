package org.oso.core.dtos

import org.oso.core.entities.Coordinate
import org.oso.core.entities.EmergencyType

data class EmergencyDto(
    var helprequester: String,
    var emergencyType: EmergencyType,
    var coordinates: Coordinate? = null
)