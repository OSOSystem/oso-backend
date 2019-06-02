package org.oso.core.services.impl

import org.oso.core.entities.Emergency
import org.oso.core.entities.EmergencyStatusType
import org.oso.core.repositories.EmergencyRepository
import org.oso.core.services.EmergencyService
import org.oso.core.services.EmergencyStatusService
import org.oso.core.services.external.NotificationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.oso.core.repositories.HelpProviderRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Service
class DefaultEmergencyService
    @Autowired
    constructor(
        private val notificationService: NotificationService,
        private val emergencyRepository: EmergencyRepository,
        private val emergencyStatusService: EmergencyStatusService,
        private val helpProviderRepository: HelpProviderRepository
    ) : EmergencyService {

    override fun emit(emergency: Emergency) {
        emergency.helpRequester.helpProviders
            .filter { it.expoPushToken != null }
            .map { notificationService.createEmergencyPushNotification(it.expoPushToken!!, emergency) }
            .let {
                if (!it.isEmpty()) {
                    notificationService.sendPushNotification(it)
                }
            }

        emergencyRepository.save(emergency)
    }

    override fun findEmergency(emergencyId: String): Emergency? {
        return emergencyRepository.findById(emergencyId).orElse(null)
    }

    override fun acceptEmergency(emergencyId: String, helpProviderId: String) {
        val emergency = findEmergency(emergencyId);
        LOGGER.debug("emergency<$emergencyId> accepted by helpProvider<$helpProviderId>")

        if (emergency == null) {
            LOGGER.warn("emergency<$emergencyId> does not exist")
            return
        }

        val helpProvider = helpProviderRepository.findById(helpProviderId).orElse(null)

        if (helpProvider == null) {
            LOGGER.warn("helpProvider<$helpProviderId> does not exist")
            return
        }

        if (!helpProvider.isAssignedTo(emergency.helpRequester)) {
            LOGGER.warn("helpProvider<$helpProviderId> is not assigned to helpRequester<${emergency.helpRequester.id}")
            LOGGER.warn("helpProvider<$helpProviderId> is not allowed to accept emergency<$emergencyId> for helpresquester<${emergency.helpRequester.id}>")
            return
        }

        emergencyStatusService.addStatus(emergency, helpProvider, EmergencyStatusType.TYPE_ACCEPTED)

        if (!emergency.active) {
            LOGGER.info("emergency<$emergencyId> is already resolved")
            return
        }
        emergency.helpRequester.helpProviders
                .filter { it.expoPushToken != null }
                .filter { it.id != helpProviderId }
                .map {
                    notificationService.createEmergencyAcceptedPushNotification(
                            to = it.expoPushToken!!,
                            alarmID = emergencyId,
                            helpRequesterId = emergency.helpRequester.id!!,
                            helpProviderId = helpProviderId
                    )
                }
                .let {
                    if(!it.isEmpty()) {
                        notificationService.sendPushNotification(it)
                    }
                }
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(DefaultEmergencyService::class.java)
    }
}