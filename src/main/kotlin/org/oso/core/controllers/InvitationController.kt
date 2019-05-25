package org.oso.core.controllers

import org.oso.config.Paths
import org.oso.core.events.InvitationEvent
import org.oso.core.services.HelpProviderService
import org.oso.core.services.HelpRequesterService
import org.oso.core.services.SecurityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.net.URL
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping(Paths.Invitation.INVITATION)
class InvitationController
    @Autowired constructor(
        private val securityService: SecurityService,
        private val eventPublisher: ApplicationEventPublisher,
        private val helpRequesterService: HelpRequesterService,
        private val helpProviderService: HelpProviderService
    ) {

    @GetMapping(Paths.Invitation.REQUEST)
    fun requestInvitation(request: HttpServletRequest, @RequestParam hrId: String, @RequestParam hpId: String): ResponseEntity<Unit> {
        val helpRequester = helpRequesterService.findById(hrId)
        val helpProvider = helpProviderService.findById(hpId)

        if (helpRequester != null && helpProvider != null) {
            eventPublisher.publishEvent(
                InvitationEvent(
                    helpRequester,
                    helpProvider,
                    url(request.scheme, request.serverName, request.localPort),
                    request.locale
                )
            )
        }

        return ResponseEntity.ok().build()
    }

    fun url(protocol: String, host: String, port: Int): URL = URL(protocol, host, port, "")

    @GetMapping(Paths.Invitation.ACCEPTED)
    fun acceptInvitation(@RequestParam token: String): String {
        val verificationToken = securityService.getVerificationToken(token)

        if (verificationToken != null && !securityService.verificationTokenExpired(verificationToken)) {
            val (_, helpRequester, helpProvider) = verificationToken
            helpRequesterService.addHelpProviderToHelpRequester(helpRequester, helpProvider)
        } else {
            return "invitationTokenExpired"
        }

        return "invitationAccepted"
    }
}