package org.oso.core.entities

import java.time.Instant
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToOne

@Entity
data class VerificationToken(
    @Id
    val token: String,
    @OneToOne
    val helpRequester: HelpRequester,
    @OneToOne
    val helpProvider: HelpProvider,
    val expiryDate: Instant = Calendar.getInstance().apply { add(Calendar.MINUTE, EXPIRATION) }.toInstant()
) {
    companion object {
        const val EXPIRATION: Int = 60 * 24
    }
}