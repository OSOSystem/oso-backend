package org.oso.core.services.impl

import org.oso.core.repositories.EmergencyActionTypeRepository
import org.oso.core.services.EmergencyActionTypeService
import org.springframework.stereotype.Service

@Service
class DefaultEmergencyActionTypeService(
    val emergencyActionTypeRepository: EmergencyActionTypeRepository
) : EmergencyActionTypeService {
    override fun findByName(name: String) =
        emergencyActionTypeRepository.findById(name).get()
}