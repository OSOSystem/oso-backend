package org.oso.core.entities

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class EmailVerificationToken(
    @Id
    val token: String,
    val helpRequester: HelpRequester,
    val helpProvider: HelpProvider,
    val expiryDate: Date
) {
    companion object {
        val EXPIRATION: Int = 60 * 24
    }
}