package org.oso.core.services.external.impl

import com.fasterxml.jackson.databind.ObjectMapper
import org.oso.core.dtos.PushNotification
import org.oso.core.entities.Emergency
import org.oso.core.factories.NotificationFactory
import org.oso.core.services.external.NotificationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.retry.support.RetryTemplate
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class ExpoNotificationService
    @Autowired constructor(
        @Value("\${expo.notifications.api.url}")
        private val url: String,
        private val restTemplate: RestTemplate,
        private val objectMapper: ObjectMapper,
        private val retryTemplate: RetryTemplate,
        private val notificationFactory: NotificationFactory
    ) : NotificationService {

    override fun sendPushNotification(pushNotifications: List<PushNotification>) {
        objectMapper
            .writeValueAsString(pushNotifications)
            .let {
                val headers = HttpHeaders()
                headers.set("accept", "application/json")
                headers.set("accept-encoding", "gzip, deflate")
                headers.set("content-type", "application/json; charset=utf-8")
                val httpEntity = HttpEntity(it, headers)
                val response = restTemplate.postForObject(url, httpEntity, String::class.java)
                val pushNotificationStatus = objectMapper.readValue(response, PushNotificationStatus::class.java)
                val failedPushNotifications = getFailedPushNotifications(pushNotificationStatus, pushNotifications)
            }
    }

    private data class PushNotificationStatus(val data: List<String>)

    private fun getFailedPushNotifications(
        pushNotificationStatus: PushNotificationStatus,
        pushNotifications: List<PushNotification>
    ) {
        pushNotificationStatus
            .data
            .mapIndexed { i, status -> Pair(i, status) }
            .filter { it.second != "ok" }
            .map { pushNotifications[it.first] }
            .let { objectMapper.writeValueAsString(it) }
    }

    override fun createEmergencyPushNotification(to: String, emergency: Emergency): PushNotification  =
        notificationFactory.createEmergencyPushNotification(
            to = to,
            emergency = emergency
        )

    override fun createEmergencyAcceptedPushNotification(to: String, alarmID: Long, helpRequesterId: Long, helpProviderId: Long): PushNotification =
        notificationFactory.createEmergencyAcceptedPushNotification(
            to = to,
            alarmID = alarmID,
            helpRequesterId = helpRequesterId,
            helpProviderId = helpProviderId
        )
}