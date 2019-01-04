package org.oso.core.services.external.impl

import com.fasterxml.jackson.databind.ObjectMapper
import org.oso.core.entities.Coordinate
import org.oso.core.services.external.GeoMapService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.retry.support.RetryTemplate
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class OpenStreetMapsService
    @Autowired
    constructor(
        private val restTemplate: RestTemplate,
        private val retryTemplate: RetryTemplate,
        private val objectMapper: ObjectMapper)
    : GeoMapService {

    /**
     * Provides an api for searching OpenStreetMap.
     *
     * Restrictions:
     * - Only 1 request per second is allowed
     * - Valid HTTP Referer or User-Agent should be included in the HTTP header
     */
    private val NOMINATIM = "https://nominatim.openstreetmap.org"

    // TODO consider the importance value in the json v2 format
    override fun resolve(coordinates: Coordinate): String? {
        val headers = HttpHeaders()
        headers.add("Referer", "https://app.ososystem.de")
        val entity = HttpEntity<String>(headers)

        val body =
            retryTemplate.execute<String, Exception> {
                val response =
                    restTemplate.exchange(
                        "$NOMINATIM/reverse?format=json&lat=${coordinates.latitude}&lon=${coordinates.longitude}",
                        HttpMethod.GET,
                        entity,
                        String::class.java
                    )

                if (response.statusCode != HttpStatus.OK) {
                    throw RuntimeException("Expecting HTTP Status Code ${HttpStatus.OK.value()}")
                }

                return@execute response.body
            }

        val json = objectMapper.readTree(body.orEmpty())

        return if (json.has("display_name")) json.get("display_name").asText() else null
    }
}