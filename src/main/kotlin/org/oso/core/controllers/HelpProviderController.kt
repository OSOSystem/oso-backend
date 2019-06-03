package org.oso.core.controllers

import org.oso.config.Paths
import org.oso.core.dtos.*
import org.oso.core.entities.HelpProvider
import org.oso.core.exceptions.HelpProviderNotFoundException
import org.oso.core.services.HelpProviderService
import org.oso.core.services.SecurityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.net.URI

@Controller
@RequestMapping(Paths.HelpProvider.PROVIDERS)
class HelpProviderController
    @Autowired
    constructor(private val helpProviderService: HelpProviderService,
                private val securityService: SecurityService) {

    @GetMapping
    @ResponseBody
    fun findAll(): List<HelpProviderDto> {
        return helpProviderService.findAll().map { it.toDto() }
    }

    @GetMapping("{id}")
    @ResponseBody
    fun findById(@PathVariable id: String): HelpProviderDto {
        return getHelpProviderOrFail(id).toDto()
    }

    val helpProvider = Paths.HelpProvider;

    @GetMapping("{id}/${Paths.HelpProvider.REQUESTERS}")
    @ResponseBody
    fun findHelpRequesters(@PathVariable id: String): List<HelpRequesterDto> {
        val helpProvider = getHelpProviderOrFail(id)
        return helpProviderService.findHelpRequesters(helpProvider.id!!).map { it.toDto() }
    }

    @PostMapping
    fun createHelpProvider(helpProvider: HelpProviderPushDto): ResponseEntity<Unit> {
        return helpProviderService.createHelpProvider(helpProvider.toEntity()).let {
            ResponseEntity.created(URI("${Paths.HelpProvider.PROVIDERS}/${it.id}")).build()
        }
    }


    private fun getHelpProviderOrFail(id: String): HelpProvider {
        return helpProviderService.findById(id) ?: throw HelpProviderNotFoundException("HelpProvider<$id> not found")
    }

    private fun HelpProviderPushDto.toEntity() = HelpProvider(
        name = name,
        keycloakName = securityService.getCurrentUserName()
    )

}