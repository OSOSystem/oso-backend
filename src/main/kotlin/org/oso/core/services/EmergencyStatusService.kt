package org.oso.core.services

import org.oso.core.entities.Emergency
import org.oso.core.entities.HelpProvider

interface EmergencyStatusService {
    fun addAction(emergency: Emergency, helpProvider: HelpProvider, action: String)
}