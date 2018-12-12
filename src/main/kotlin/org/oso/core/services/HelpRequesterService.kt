package org.oso.core.services

import org.oso.core.entities.HelpProvider
import org.oso.core.entities.HelpRequester

interface HelpRequesterService {
    fun findById(id: String): HelpRequester?
    fun findHelpProviders(helpRequesterId: String): Set<HelpProvider>?
}