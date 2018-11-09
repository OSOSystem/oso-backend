package org.oso.core.controllers

import org.oso.core.dtos.HelpProviderDto
import org.oso.core.dtos.toDto
import org.oso.core.entities.HelpProvider
import org.oso.core.exceptions.HelpRequesterNotFoundException
import org.oso.core.services.HelpRequesterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping(HelpRequesterController.PATH_HELP_REQUESTERS)
class HelpRequesterController
    @Autowired
    constructor(private val helpRequesterService: HelpRequesterService) {

    @GetMapping("{id}/$PATH_HELP_PROVIDERS")
    @ResponseBody
    fun findHelpProviders(@PathVariable id: Long): List<HelpProviderDto> {
        return helpRequesterService.findHelpProviders(id)?.map { it.toDto() }?: throw HelpRequesterNotFoundException("HR with id $id not found")
    }

    companion object {
        const val PATH_HELP_REQUESTERS = "help-requesters"
        const val PATH_HELP_PROVIDERS = "help-providers"
    }
}