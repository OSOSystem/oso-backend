package org.oso.core.services

import org.oso.core.entities.HelpProvider
import org.oso.core.entities.HelpRequester

interface HelpProviderService {
    fun findAll(): List<HelpProvider>
    fun findById(id: Long): HelpProvider?
    fun findHelpRequesters(id: Long): Set<HelpRequester>
    fun createHelpProvider(helpProvider: HelpProvider): HelpProvider
    fun acceptEmergency(emergencyId: Long, helpProviderId: Long)
}