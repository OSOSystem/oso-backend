package org.oso.core.services

import org.oso.core.entities.EmailVerificationToken
import org.oso.core.entities.HelpProvider
import org.oso.core.entities.HelpRequester

/**
 * This service simplifies and expands handling security related topics.
 */
interface SecurityService {
    fun getCurrentUserName(): String
    fun createEmailVerificationToken(helpRequester: HelpRequester, helpProvider: HelpProvider): EmailVerificationToken
    fun getEmailVerificationToken(id: String): EmailVerificationToken?
    fun tokenExpired(token: EmailVerificationToken): Boolean
}