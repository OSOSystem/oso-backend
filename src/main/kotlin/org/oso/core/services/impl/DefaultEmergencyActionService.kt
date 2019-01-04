package org.oso.core.services.impl

import org.oso.core.entities.Emergency
import org.oso.core.entities.EmergencyAction
import org.oso.core.entities.HelpProvider
import org.oso.core.repositories.EmergencyActionRepository
import org.oso.core.services.EmergencyActionService
import org.springframework.stereotype.Service

@Service
class DefaultEmergencyActionService(
    val emergencyActionRepository: EmergencyActionRepository,
    val emergencyActionTypeService: DefaultEmergencyActionTypeService
) : EmergencyActionService {

    override fun addAction(emergency: Emergency, helpProvider: HelpProvider, action: String) {
        emergencyActionRepository.save(
            EmergencyAction(
                emergency = emergency,
                helpProvider = helpProvider,
                type = emergencyActionTypeService.findByName(action)
            )
        )
    }
}