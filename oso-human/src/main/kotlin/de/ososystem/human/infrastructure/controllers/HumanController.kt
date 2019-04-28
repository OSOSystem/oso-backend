package de.ososystem.human.infrastructure.controllers

import de.ososystem.human.domain.dtos.HumanDto
import de.ososystem.human.domain.entities.Human
import de.ososystem.human.domain.exceptions.HumanNotFoundException
import de.ososystem.human.domain.services.HumanService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.*

@Controller
@RequestMapping(HumanController.PATH_HUMAN)
class HumanController(
    val humanService: HumanService
) {

    @GetMapping
    @ResponseBody
    fun getAll(): List<Human> {
        return humanService.getAllHumans().toList()
    }

    @GetMapping("{id}")
    @ResponseBody
    fun get(@PathVariable id: UUID): Human {
        return humanService.getHuman(id) ?: throw HumanNotFoundException("Human $id does not exist")
    }

    @PostMapping
    fun createHuman(@RequestBody humanDto: HumanDto): ResponseEntity<Human> {
        return humanService.createHuman(humanDto).let {
            ResponseEntity.created(URI("$PATH_HUMAN/{${it.id}")).build()
        }
    }

    companion object {
        const val PATH_HUMAN = "humans"
    }
}

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class HumanNotFoundExceptionHttp(
    message: String
) : Exception(message)