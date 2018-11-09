package org.oso.core.entities

import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
data class EmergencyAction(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emergency_id")
    val emergency: Emergency,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "helpprovider_id")
    val helpProvider: HelpProvider,
    val action: Action
) : BaseEntity()

enum class Action {
    ACCEPT,
    CANCEL_ACCEPT,
    RESOLVE,
    CANCEL_RESOLVE
}