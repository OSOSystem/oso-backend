package org.oso.core.entities

import java.time.LocalDateTime
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
    val expiryDate: LocalDateTime = LocalDateTime.now().plusMinutes(EXPIRATION)
) {
    companion object {
        const val EXPIRATION: Long = 60 * 24
    }
}