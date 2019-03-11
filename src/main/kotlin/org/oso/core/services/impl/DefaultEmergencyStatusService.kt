package org.oso.core.services.impl

import org.oso.core.entities.Emergency
import org.oso.core.entities.EmergencyStatus
import org.oso.core.entities.HelpProvider
import org.oso.core.repositories.EmergencyStatusRepository
import org.oso.core.services.EmergencyStatusService
import org.springframework.stereotype.Service

@Service
class DefaultEmergencyStatusService(
    val emergencyStatusRepository: EmergencyStatusRepository,
    val emergencyStatusTypeService: DefaultEmergencyStatusTypeService
) : EmergencyStatusService {

    override fun addStatus(emergency: Emergency, helpProvider: HelpProvider, status: String) {
        emergencyStatusRepository.save(
            EmergencyStatus(
                emergency = emergency,
                helpProvider = helpProvider,
                type = emergencyStatusTypeService.findByName(status)
            )
        )
    }
}