package org.oso.core.dtos

import org.oso.core.entities.Coordinates
import org.oso.core.entities.EmergencyType

class EmergencyDto(
    var helprequester: Long,
    var emergencyType: EmergencyType,
    var coordinates: Coordinates? = null
)