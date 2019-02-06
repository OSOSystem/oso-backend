package org.oso.core.factories

import com.fasterxml.jackson.databind.ObjectMapper
import org.oso.core.dtos.PushNotification
import org.oso.core.entities.Coordinate
import org.oso.core.entities.Emergency
import org.oso.core.entities.EmergencyType
import org.oso.core.services.external.GeoMapService
import org.springframework.stereotype.Component

@Component
class NotificationFactory constructor(
    private val objectMapper: ObjectMapper,
    private val geoMapService: GeoMapService
) {

    fun createEmergencyPushNotification(to: String, emergency: Emergency): PushNotification {

        val helpRequesterId = emergency.helpRequester.id
        val coordinates = emergency.coordinates
        val formattedAddress = coordinates?.let { geoMapService.resolve(it) }

        return PushNotification(
                to,
                objectMapper.writeValueAsString(
                    PushNotificationEmergency(
                        helpRequesterId!!,
                        emergency.emergencyType,
                        coordinates,
                        formattedAddress
                    )
                ),
                "Emergency",
                "Emergency"
        )
    }

    fun createEmergencyAcceptedPushNotification(to: String, alarmId: String, helpRequesterId: String, helpProviderId: String) =
        PushNotification(
            to,
            objectMapper.writeValueAsString(
                PushNotificationEmergencyAccepted(
                    emergencyId = alarmId,
                    helpRequesterId = helpRequesterId,
                    helpProviderId =  helpProviderId
                )
            ),
            "Emergency resolved",
            "Emergency resolved"
        )
}

private class PushNotificationEmergency(
    val helpRequesterId: String,
    val emergencyType: EmergencyType,
    val coordinates: Coordinate?,
    val formattedAddress: String?
)

private data class PushNotificationEmergencyAccepted(
    val emergencyId: String,
    val helpRequesterId: String,
    val helpProviderId: String
)