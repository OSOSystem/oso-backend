package org.oso.core.services.external

import org.oso.core.dtos.PushNotification
import org.oso.core.entities.Emergency

/**
 * Represents a service which sends notifications to mobile devices.
 * The notification handling concerns only the help providers.
 */
interface NotificationService {
    /**
     * Sends push notifications to mobile devices.
     * The [pushNotifications] are multiplexed into one json object.
     * @param pushNotifications The push notifications to send.
     */
    fun sendPushNotification(pushNotifications: List<PushNotification>)

    fun createEmergencyPushNotification(to: String, emergency: Emergency): PushNotification
    fun createEmergencyAcceptedPushNotification(to: String, alarmID: Long, helpRequesterId: Long, helpProviderId:  Long): PushNotification
}