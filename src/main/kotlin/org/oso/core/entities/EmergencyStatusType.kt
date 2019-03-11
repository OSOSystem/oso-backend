package org.oso.core.entities

import javax.persistence.Entity
import javax.persistence.Id
import javax.validation.constraints.Size

@Entity
class EmergencyStatusType(
    @Id
    @Size(min = 1)
    val name: String
) {
    companion object {
        const val TYPE_ACCEPTED = "accepted"
        const val TYPE_CANCEL_ACCEPT = "cancelAccept"
        const val TYPE_RESOLVED = "resolved"
        const val TYPE_CANCEL_RESOLVE = "cancelResolve"
    }
}