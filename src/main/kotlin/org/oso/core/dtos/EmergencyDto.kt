package org.oso.core.dtos

import org.oso.core.entities.Coordinate
import org.oso.core.entities.EmergencyPriority

data class EmergencyDto(
    var helprequester: String,
    var emergencyPriority: EmergencyPriority,
    var coordinates: Coordinate? = null
)