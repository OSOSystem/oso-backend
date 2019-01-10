package org.oso.core.services.impl

import org.oso.core.entities.EmailVerificationToken
import org.oso.core.entities.HelpProvider
import org.oso.core.entities.HelpRequester
import org.oso.core.repositories.EmailVerificationTokenRepository
import org.oso.core.services.SecurityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class DefaultSecurityService
    @Autowired constructor(private val emailVerificationTokenRepository: EmailVerificationTokenRepository) : SecurityService {

    override fun getCurrentUserName() =
        SecurityContextHolder.getContext().authentication.name

    override fun createEmailVerificationToken(helpRequester: HelpRequester, helpProvider: HelpProvider): EmailVerificationToken {
        return EmailVerificationToken(UUID.randomUUID().toString(), helpRequester, helpProvider, Date.from(Instant.now()))
            .let { emailVerificationTokenRepository.save(it) }
    }

    override fun getEmailVerificationToken(id: String): EmailVerificationToken? {
        return emailVerificationTokenRepository.findById(id).orElse(null)
    }

    override fun tokenExpired(token: EmailVerificationToken): Boolean {
        return (token.expiryDate.time - Date.from(Instant.now()).time) < 0
    }
}