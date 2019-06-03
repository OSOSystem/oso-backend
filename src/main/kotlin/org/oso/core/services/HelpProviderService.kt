package org.oso.core.services

import org.oso.core.entities.HelpProvider
import org.oso.core.entities.HelpRequester

interface HelpProviderService {
    fun findAll(): List<HelpProvider>
    fun findById(id: String): HelpProvider?
    fun findHelpRequesters(id: String): Set<HelpRequester>
    fun createHelpProvider(helpProvider: HelpProvider): HelpProvider
}