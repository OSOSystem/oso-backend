package de.ososystem.human.infrastructure.controllers

import de.ososystem.human.domain.dtos.HumanDto
import de.ososystem.human.domain.entities.Human
import de.ososystem.human.domain.exceptions.HumanAlreadyExistsException
import de.ososystem.human.domain.exceptions.HumanException
import de.ososystem.human.domain.exceptions.HumanNotFoundException
import de.ososystem.human.domain.services.HumanService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.*

@Controller
@RequestMapping(HumanController.PATH_HUMAN)
class HumanController(
    val humanService: HumanService
) {

    @GetMapping
    @Transactional
    @ResponseBody
    fun getAll(): List<Human> {
        return humanService.getAllHumans().toList()
    }

    @GetMapping("{id}")
    @ResponseBody
    @Transactional
    fun get(@PathVariable id: UUID): Human {
        return humanService.getHuman(id) ?: throw HumanNotFoundExceptionHttp("Human $id does not exist")
    }

    @PostMapping
    @Transactional
    fun createHuman(@RequestBody humanDto: HumanDto): ResponseEntity<Human> {
        try {
            return humanService.createHuman(humanDto).let {
                ResponseEntity.created(URI("$PATH_HUMAN/${it.id}")).build()
            }
        } catch (e: HumanException) {
            throw e.toHttpException()
        }
    }

    companion object {
        const val PATH_HUMAN = "humans"
    }

    fun HumanException.toHttpException()
        = when (this) {
            is HumanNotFoundException -> HumanNotFoundExceptionHttp(message, this)
            is HumanAlreadyExistsException -> HumanAlreadyExistsExceptionHttp(message, this)
            // TODO should never happen
            else -> this
        }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    class HumanNotFoundExceptionHttp(
        message: String?,
        e: HumanNotFoundException? = null
    ) : Exception(message, e)

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    class HumanAlreadyExistsExceptionHttp (
        message: String?,
        e: HumanAlreadyExistsException? = null
    ) : Exception(message, e)
}