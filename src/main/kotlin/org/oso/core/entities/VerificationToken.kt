package org.oso.core.entities

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
    val expiryDate: Date = Calendar.getInstance().apply { add(Calendar.MINUTE, EXPIRATION) }.time
) {
    companion object {
        const val EXPIRATION: Int = 60 * 24
    }
}