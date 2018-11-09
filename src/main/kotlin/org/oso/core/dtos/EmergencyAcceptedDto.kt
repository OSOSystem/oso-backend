package org.oso.core.dtos

data class EmergencyAcceptedDto(
    var emergencyId: Long,
    var helpRequesterId: Long,
    var helpProviderId: Long
)