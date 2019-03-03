package org.oso.core.services.impl

import org.oso.core.repositories.EmergencyStatusTypeRepository
import org.oso.core.services.EmergencyStatusTypeService
import org.springframework.stereotype.Service

@Service
class DefaultEmergencyActionTypeService(
    val emergencyStatusTypeRepository: EmergencyStatusTypeRepository
) : EmergencyStatusTypeService {
    override fun findByName(name: String) =
        emergencyStatusTypeRepository.findById(name).get()
}