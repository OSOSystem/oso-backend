package org.oso.core.entities

import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
class Emergency (
    @Id
    @Column(nullable = false)
    @Size(min = 1)
    var id: String? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "helpRequester_id")
    val helpRequester: HelpRequester,
    var emergencyPriority: EmergencyPriority = EmergencyPriority.LOW,
    var time: LocalDateTime = LocalDateTime.now(),
    val coordinates: Coordinate? = null
) {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "emergency")
    val status = mutableSetOf<EmergencyStatus>()

    val active: Boolean
        get() {
            val listStatus = status.sortedByDescending { it.time }

            // TODO overthink this logic when handling of this actions is finished
            for(status in listStatus) {
                // we go through the actions in descending order by their time ...
                when (status.type.name) {
                    // so if the last action was a resolve, the emergency is resolved
                    EmergencyStatusType.TYPE_RESOLVED -> return false
                    // and if the last action is a cancel, than the alarm is still (again) active
                    EmergencyStatusType.TYPE_CANCEL_RESOLVE -> return true
                    else -> {
                    }
                }
            }

            return true
        }
}