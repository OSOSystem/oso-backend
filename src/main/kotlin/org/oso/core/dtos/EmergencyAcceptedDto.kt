package org.oso.core.dtos

data class EmergencyAcceptedDto(
    var emergencyId: String,
    var helpRequesterId: String,
    var helpProviderId: String
)