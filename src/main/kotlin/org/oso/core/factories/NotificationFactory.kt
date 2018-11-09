package org.oso.core.factories

import com.fasterxml.jackson.databind.ObjectMapper
import org.oso.core.dtos.PushNotification
import org.oso.core.entities.Coordinates
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

    fun createEmergencyAcceptedPushNotification(to: String, alarmID: Long, helpRequesterId: Long, helpProviderId:  Long) =
        PushNotification(
            to,
            objectMapper.writeValueAsString(
                PushNotificationEmergencyAccepted(
                    emergencyId = alarmID,
                    helpRequesterId = helpRequesterId,
                    helpProviderId =  helpProviderId
                )
            ),
            "Emergency resolved",
            "Emergency resolved"
        )
}

private class PushNotificationEmergency(
    val helpRequesterId: Long,
    val emergencyType: EmergencyType,
    val coordinates: Coordinates?,
    val formattedAddress: String?
)

private data class PushNotificationEmergencyAccepted(
    val emergencyId: Long,
    val helpRequesterId: Long,
    val helpProviderId: Long
)