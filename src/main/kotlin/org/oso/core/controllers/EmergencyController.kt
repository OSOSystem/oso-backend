package org.oso.core.controllers

import org.oso.config.Paths
import org.oso.core.dtos.*
import org.oso.core.entities.Emergency
import org.oso.core.exceptions.HelpRequesterNotFoundException
import org.oso.core.services.EmergencyService
import org.oso.core.services.HelpRequesterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*


@Controller
@RequestMapping(Paths.Emergency.ROOT)
class EmergencyController
    @Autowired
    constructor(
        private val emergencyService: EmergencyService,
        private val helpRequesterService: HelpRequesterService) {

    @PostMapping(Paths.Emergency.EMIT)
    @ResponseStatus(HttpStatus.CREATED)
    fun emit(@RequestBody emergencyDto: EmergencyDto) {

        val helpRequester = helpRequesterService.findById(emergencyDto.helprequester) ?:
            throw HelpRequesterNotFoundException("HelpRequester<${emergencyDto.helprequester}> does not exist")

        val emergency = Emergency(
            helpRequester = helpRequester,
            emergencyPriority = emergencyDto.emergencyPriority,
            coordinates = emergencyDto.coordinates
        )

        emergencyService.emit(emergency)
    }

    @PostMapping(Paths.Emergency.ACCEPTED)
    fun acceptEmergency(@RequestBody emergencyAccepted: EmergencyAcceptedDto): ResponseEntity<Unit> {
        emergencyService.acceptEmergency(
                emergencyId = emergencyAccepted.emergencyId,
                helpProviderId = emergencyAccepted.helpProviderId
        )

        return ResponseEntity.accepted().build<Unit>()
    }

}