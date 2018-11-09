package org.oso.core.services.impl

import org.oso.core.entities.HelpProvider
import org.oso.core.entities.HelpRequester
import org.oso.core.exceptions.HelpRequesterNotFoundException
import org.oso.core.repositories.HelpRequesterRepository
import org.oso.core.services.HelpRequesterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DefaultHelpRequesterService
    @Autowired
    constructor(private val helpRequesterRepository: HelpRequesterRepository) : HelpRequesterService {

    override fun findById(id: Long): HelpRequester? = helpRequesterRepository.findById(id).orElse(null)

    override fun findHelpProviders(helpRequesterId: Long): Set<HelpProvider>? =
        findById(helpRequesterId)
            ?.helpProviders
            ?: setOf()
}