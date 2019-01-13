package org.oso.core.controllers

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
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping(InvitationController.PATH_INVITATION)
class InvitationController
    @Autowired constructor(
        private val securityService: SecurityService,
        private val eventPublisher: ApplicationEventPublisher,
        private val helpRequesterService: HelpRequesterService,
        private val helpProviderService: HelpProviderService
    ) {

    @GetMapping(PATH_REQUEST)
    fun requestInvitation(request: HttpServletRequest, @RequestParam hrId: String, @RequestParam hpId: String): ResponseEntity<Unit> {
        val helpRequester = helpRequesterService.findById(hrId)
        val helpProvider = helpProviderService.findById(hpId)

        if (helpRequester != null && helpProvider != null) {
            eventPublisher.publishEvent(
                InvitationEvent(
                    helpRequester,
                    helpProvider,
                    "${request.scheme}://${request.serverName}:${request.localPort}",
                    request.locale
                )
            )
        }

        return ResponseEntity.ok().build()
    }

    @GetMapping(PATH_ACCEPT)
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

    companion object {
        const val PATH_INVITATION = "invitation"
        const val PATH_REQUEST = "request"
        const val PATH_ACCEPT = "accept"
    }
}