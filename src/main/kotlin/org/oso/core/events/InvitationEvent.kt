package org.oso.core.events

import org.oso.core.entities.HelpProvider
import org.oso.core.entities.HelpRequester
import org.springframework.context.ApplicationEvent
import java.util.*

data class InvitationEvent(
    val helpRequester: HelpRequester,
    val helpProvider: HelpProvider,
    val locale: Locale
) : ApplicationEvent(helpProvider)