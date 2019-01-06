package org.oso.core.listeners

import org.oso.core.entities.EmailVerificationToken
import org.oso.core.events.InvitationEvent
import org.oso.core.services.MailService
import org.oso.core.services.SecurityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationListener
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component

@Component
class InvitationListener
    @Autowired constructor(
        @Value("\${email.from}")
        private val from: String,
        private val mailService: MailService,
        private val messageSource: MessageSource,
        private val securityService: SecurityService
    ) : ApplicationListener<InvitationEvent> {

    override fun onApplicationEvent(event: InvitationEvent) {
        val (helpRequester, helpProvider, locale) = event
        val verificationToken = securityService.createEmailVerificationToken(helpRequester, helpProvider)

        val subject =
            messageSource.getMessage("invitation.subject", arrayOf(helpRequester.keycloakName), locale)
        val text =
            messageSource
                .getMessage(
                    "invitation.text",
                    arrayOf(
                        helpRequester.keycloakName,
                        "http://localhost:8081/invitation/accept?token=${verificationToken.token}",
                        "${EmailVerificationToken.EXPIRATION / 60}",
                        "h"
                    ),
                    locale
                )

        mailService.send(
            from,
            listOf(helpProvider.keycloakName),
            subject,
            text
        )
    }
}