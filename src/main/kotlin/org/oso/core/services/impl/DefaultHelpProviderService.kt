package org.oso.core.services.impl

import org.oso.core.entities.Action
import org.oso.core.entities.HelpProvider
import org.oso.core.entities.HelpRequester
import org.oso.core.repositories.HelpProviderRepository
import org.oso.core.services.EmergencyActionService
import org.oso.core.services.EmergencyService
import org.oso.core.services.HelpProviderService
import org.oso.core.services.external.NotificationService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DefaultHelpProviderService
    @Autowired
    constructor(
        private val notificationService: NotificationService,
        private val emergencyActionService: EmergencyActionService,
        private val helpProviderRepository: HelpProviderRepository,
        private val emergencyService: EmergencyService
    ) : HelpProviderService {

    override fun findAll(): List<HelpProvider> {
        return helpProviderRepository.findAll().toList()
    }

    override fun findById(id: Long): HelpProvider? {
        return helpProviderRepository.findById(id).orElse(null)
    }

    override fun findHelpRequesters(id: Long): Set<HelpRequester> {
        val helpProvider = findById(id)
        return helpProvider?.helpRequesters.orEmpty()
    }

    override fun createHelpProvider(helpProvider: HelpProvider): HelpProvider =
        helpProviderRepository.save(helpProvider)

    override fun acceptEmergency(emergencyId: Long, helpProviderId: Long) {
        val emergency = emergencyService.findEmergency(emergencyId);
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

        emergencyActionService.addAction(emergency, helpProvider, Action.ACCEPT)

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
        private val LOGGER: Logger = LoggerFactory.getLogger(DefaultHelpProviderService::class.java)
    }
}