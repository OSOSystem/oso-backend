package org.oso.core.services

import org.oso.core.entities.Action
import org.oso.core.entities.Emergency
import org.oso.core.entities.HelpProvider

/**
 * Represents a service for emitting and handling emergencies.
 */
interface EmergencyService {
    fun findEmergency(emergencyId: Long): Emergency?
    /**
     * Emits an emergency by notifying every hp assigned to a hr.
     *
     * @param emergency The emergency containing relevant information
     */
    fun emit(emergency: Emergency)
}