package org.oso.core.services.external

import org.oso.core.entities.Coordinate

/**
 * Represents a service which uses a geographical map api.
 * Coordinates consist of latitude and longitude.
 */
interface GeoMapService {
    /**
     * Resolves an address wit the given [coordinates].
     *
     * @param coordinates The latitude and longitude which are going to be resolved
     */
    fun resolve(coordinates: Coordinate): String?
}