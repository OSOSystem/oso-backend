package org.oso.core.services.impl

import org.oso.core.entities.EmergencyStatusType
import org.oso.core.entities.HelpProvider
import org.oso.core.entities.HelpRequester
import org.oso.core.repositories.HelpProviderRepository
import org.oso.core.services.EmergencyStatusService
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
        private val helpProviderRepository: HelpProviderRepository,
        private val emergencyService: EmergencyService
    ) : HelpProviderService {

    override fun findAll(): List<HelpProvider> {
        return helpProviderRepository.findAll().toList()
    }

    override fun findById(id: String): HelpProvider? {
        return helpProviderRepository.findById(id).orElse(null)
    }

    override fun findHelpRequesters(id: String): Set<HelpRequester> {
        val helpProvider = findById(id)
        return helpProvider?.helpRequesters.orEmpty()
    }

    override fun createHelpProvider(helpProvider: HelpProvider): HelpProvider =
        helpProviderRepository.save(helpProvider)


}