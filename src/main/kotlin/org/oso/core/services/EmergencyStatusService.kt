package org.oso.core.services

import org.oso.core.entities.Emergency
import org.oso.core.entities.HelpProvider

interface EmergencyStatusService {
    fun addStatus(emergency: Emergency, helpProvider: HelpProvider, status: String)
}