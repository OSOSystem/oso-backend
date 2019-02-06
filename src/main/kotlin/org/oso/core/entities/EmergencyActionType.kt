package org.oso.core.entities

import javax.persistence.Entity
import javax.persistence.Id
import javax.validation.constraints.Size

@Entity
class EmergencyActionType(
    @Id
    @Size(min = 1)
    val name: String
) {
    companion object {
        const val TYPE_ACCEPT = "accept"
        const val TYPE_CANCEL_ACCEPT = "cancelAccept"
        const val TYPE_RESOLVE = "resolve"
        const val TYPE_CANCEL_RESOLVE = "cancelResolve"
    }
}