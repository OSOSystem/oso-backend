package org.oso.core.services

import org.oso.core.entities.HelpProvider
import org.oso.core.entities.HelpRequester
import org.oso.core.entities.VerificationToken

/**
 * This service simplifies and expands handling security related topics.
 */
interface SecurityService {
    fun getCurrentUserName(): String
    fun createVerificationToken(helpRequester: HelpRequester, helpProvider: HelpProvider): VerificationToken
    fun getVerificationToken(id: String): VerificationToken?
    fun verificationTokenExpired(token: VerificationToken): Boolean
}