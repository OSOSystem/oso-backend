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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.context.request.WebRequest

@Controller("invitation")
class InvitationController
    @Autowired constructor(
        private val securityService: SecurityService,
        private val eventPublisher: ApplicationEventPublisher,
        private val helpRequesterService: HelpRequesterService,
        private val helpProviderService: HelpProviderService
    ) {

    @GetMapping("request")
    fun request(webRequest: WebRequest, @RequestParam hrId: String, @RequestParam hpId: String): ResponseEntity<Unit> {
        val helpRequester = helpRequesterService.findById(hrId)
        val helpProvider = helpProviderService.findById(hpId)

        if (helpRequester != null && helpProvider != null) {
            eventPublisher.publishEvent(InvitationEvent(helpRequester, helpProvider, webRequest.locale))
        }

        return ResponseEntity.ok().build()
    }

    @GetMapping("accept")
    fun accept(@RequestParam token: String) {
        val verificationToken = securityService.getEmailVerificationToken(token)

        if (verificationToken != null && !securityService.tokenExpired(verificationToken)) {
            val (_, helpRequester, helpProvider) = verificationToken
            helpRequester.helpProviders.add(helpProvider)
            helpProvider.helpRequesters.add(helpRequester)
        }
    }
}