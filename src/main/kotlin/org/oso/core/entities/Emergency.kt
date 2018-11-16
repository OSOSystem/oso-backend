package org.oso.core.entities

import javax.persistence.*

@Entity
data class Emergency (
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "helpRequester_id")
    val helpRequester: HelpRequester,
    var emergencyType: EmergencyType = EmergencyType.LOW,
    val coordinates: Coordinates? = null
): BaseEntity() {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "emergency")
    val actions = mutableSetOf<EmergencyAction>()

    val active: Boolean
        get() {
            val listActions = actions.sortedByDescending { it.updated }

            // TODO overthink this logic when handling of this actions is finished
            for(action in listActions) {
                // we go through the actions in descending order by their time ...
                when (action.action) {
                    // so if the last action was a resolve, the emergency is resolved
                    Action.RESOLVE -> return false
                    // and if the last action is a cancel, than the alarm is still (again) active
                    Action.CANCEL_RESOLVE -> return true
                    else -> {
                    }
                }
            }

            return true
        }
}