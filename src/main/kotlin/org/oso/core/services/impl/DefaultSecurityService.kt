package org.oso.core.services.impl

import org.oso.core.entities.HelpProvider
import org.oso.core.entities.HelpRequester
import org.oso.core.entities.VerificationToken
import org.oso.core.repositories.VerificationTokenRepository
import org.oso.core.services.SecurityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class DefaultSecurityService
    @Autowired constructor(private val verificationTokenRepository: VerificationTokenRepository) : SecurityService {

    override fun getCurrentUserName() =
        SecurityContextHolder.getContext().authentication.name

    override fun createVerificationToken(helpRequester: HelpRequester, helpProvider: HelpProvider): VerificationToken {
        return VerificationToken(UUID.randomUUID().toString(), helpRequester, helpProvider)
            .let { verificationTokenRepository.save(it) }
    }

    override fun getVerificationToken(id: String): VerificationToken? {
        return verificationTokenRepository.findById(id).orElse(null)
    }

    override fun verificationTokenExpired(token: VerificationToken): Boolean {
        return token.expiryDate.isBefore(Instant.now())
    }
}