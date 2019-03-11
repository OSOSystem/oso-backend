package org.oso.core.controllers

import org.oso.core.dtos.EmergencyDto
import org.oso.core.entities.Emergency
import org.oso.core.exceptions.HelpRequesterNotFoundException
import org.oso.core.services.EmergencyService
import org.oso.core.services.HelpRequesterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus

@Controller
@RequestMapping(EmergencyController.PATH_EMERGENCY)
class EmergencyController
    @Autowired
    constructor(
        private val emergencyService: EmergencyService,
        private val helpRequesterService: HelpRequesterService) {

    @PostMapping(PATH_EMIT)
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

    companion object {
        const val PATH_EMERGENCY = "emergency"
        const val PATH_EMIT = "emit"
    }
}