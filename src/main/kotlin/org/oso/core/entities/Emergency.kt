package org.oso.core.entities

import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
class Emergency (
    @Id @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(nullable = false)
    @Size(min = 1)
    var id: String? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "helpRequester_id")
    val helpRequester: HelpRequester,
    var emergencyType: EmergencyType = EmergencyType.LOW,
    var time: LocalDateTime = LocalDateTime.now(),
    val coordinates: Coordinate? = null
) {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "emergency")
    val actions = mutableSetOf<EmergencyAction>()

    val active: Boolean
        get() {
            val listActions = actions.sortedByDescending { it.time }

            // TODO overthink this logic when handling of this actions is finished
            for(action in listActions) {
                // we go through the actions in descending order by their time ...
                when (action.type.name) {
                    // so if the last action was a resolve, the emergency is resolved
                    EmergencyActionType.TYPE_RESOLVE -> return false
                    // and if the last action is a cancel, than the alarm is still (again) active
                    EmergencyActionType.TYPE_CANCEL_RESOLVE -> return true
                    else -> {
                    }
                }
            }

            return true
        }
}