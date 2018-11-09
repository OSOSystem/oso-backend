package org.oso.core.services

import org.oso.core.entities.HelpProvider
import org.oso.core.entities.HelpRequester

interface HelpRequesterService {
    fun findById(id: Long): HelpRequester?
    fun findHelpProviders(helpRequesterId: Long): Set<HelpProvider>?
}