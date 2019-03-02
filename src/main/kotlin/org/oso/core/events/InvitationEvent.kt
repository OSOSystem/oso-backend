package org.oso.core.events

import org.oso.core.entities.HelpProvider
import org.oso.core.entities.HelpRequester
import org.springframework.context.ApplicationEvent
import java.net.URL
import java.util.*

data class InvitationEvent(
    val helpRequester: HelpRequester,
    val helpProvider: HelpProvider,
    val baseUrl: URL,
    val locale: Locale
) : ApplicationEvent(helpProvider)