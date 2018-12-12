package org.oso.core.services

import org.oso.core.entities.Emergency

/**
 * Represents a service for emitting and handling emergencies.
 */
interface EmergencyService {
    fun findEmergency(emergencyId: String): Emergency?
    /**
     * Emits an emergency by notifying every hp assigned to a hr.
     *
     * @param emergency The emergency containing relevant information
     */
    fun emit(emergency: Emergency)
}