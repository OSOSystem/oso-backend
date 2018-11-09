package org.oso.core.services.impl

import org.oso.core.entities.Emergency
import org.oso.core.repositories.EmergencyRepository
import org.oso.core.services.EmergencyService
import org.oso.core.services.external.NotificationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DefaultEmergencyService
    @Autowired
    constructor(
        private val notificationService: NotificationService,
        private val emergencyRepository: EmergencyRepository
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

    override fun findEmergency(emergencyId: Long): Emergency? {
        return emergencyRepository.findById(emergencyId).orElse(null)
    }
}