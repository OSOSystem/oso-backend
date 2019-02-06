package org.oso.core.entities

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@IdClass(EmergencyActionId::class)
class EmergencyAction(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emergency_id")
    @Id
    val emergency: Emergency,
    @Id
    val time: LocalDateTime = LocalDateTime.now(),
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "helpprovider_id")
    val helpProvider: HelpProvider,
    @ManyToOne
    @JoinColumn(name ="EmergencyAction_name")
    val type: EmergencyActionType
)

data class EmergencyActionId(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emergency_id")
    @Id
    val emergency: Emergency,
    @Id
    val time: LocalDateTime = LocalDateTime.now()
) : Serializable