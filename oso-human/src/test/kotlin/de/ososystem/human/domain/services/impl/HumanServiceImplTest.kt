package de.ososystem.human.domain.services.impl

import de.ososystem.human.any
import de.ososystem.human.domain.dtos.HumanDto
import de.ososystem.human.domain.entities.Human
import de.ososystem.human.domain.exceptions.HumanAlreadyExistsException
import de.ososystem.human.domain.exceptions.HumanNotFoundException
import de.ososystem.human.domain.factories.HumanFactory
import de.ososystem.human.domain.repositories.HumanRepository
import de.ososystem.human.domain.services.*
import de.ososystem.human.mock
import de.ososystem.human.verifyOnce
import org.junit.Assert
import org.junit.Before
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.*
import java.util.*

class HumanServiceImplTest {
    val humanFactory: HumanFactory = mock()
    val humanRepository: HumanRepository = mock()
    val eventService: EventService = mock()

    val humanService = HumanServiceImpl(humanFactory, humanRepository, eventService)

    @Test
    fun testGetHuman() {
        val id = UUID.fromString("6731dc31-dfa5-4522-bed9-27e9f3d0fac6")
        val human = Human(
            id,
            "test",
            "test"
        )

        Mockito.`when`(humanRepository.findHumanById(id)).thenReturn(human)

        Assert.assertEquals("wrong human received", human, humanService.getHuman(id))
    }

    @Test
    fun testGetAllHumans() {
        val humans = listOf(
            Human(
                UUID.fromString("59a860d4-4c7d-4a8a-acb0-c2e0551494cb"),
                "bla",
                "bla"
            ),
            Human(
                UUID.fromString("e24bba4c-d5d1-405c-b92c-a6b073f1d4cf"),
                "bli",
                "bli"
            ),
            Human(
                UUID.fromString("aa49082e-ad86-4d16-aa49-f30fa91b8cd1"),
                "blup",
                "blup"
            )
        )

        Mockito.`when`(humanRepository.findAllHumans()).thenReturn(humans)

        Assert.assertEquals("Wrong humans received", humans, humanService.getAllHumans())
    }

    @Test
    fun testDeleteHuman() {
        val human = Human(
            UUID.fromString("99cfdc61-161d-4b1d-b174-9e9146244387"),
            "name",
            "test"
        )

        Mockito.`when`(humanRepository.findHumanByName(human.name)).thenReturn(human)
        Mockito.doNothing().`when`(eventService).fireHumanEvent(DOMAIN_HUMAN, TYPE_DELETED, human)

        humanService.deleteHuman(human.name)

        verifyOnce(humanRepository).deleteHuman(human)
        verifyOnce(eventService).fireHumanEvent(DOMAIN_HUMAN, TYPE_DELETED, human)
    }

    @Test
    fun testDeleteHuman_HumanDoesNotExist() {
        val name = "name"

        Mockito.`when`(humanRepository.findHumanByName(name)).thenReturn(null)

        humanService.deleteHuman(name)

        Mockito.verify(humanRepository, Mockito.never()).deleteHuman(any())
        Mockito.verify(eventService, Mockito.never()).fireHumanEvent(any(), any(), any())
    }

    @Test
    fun testChangeHuman() {
        val human = Human(
            UUID.fromString("25dc70a4-e079-401c-9b20-f5ad18c14fee"),
            "orig",
            "orig"
        )
        val humanChange = Human(
            human.id,
            "test",
            "test"
        )

        Mockito.`when`(humanRepository.findHumanById(human.id)).thenReturn(human)
        Mockito.`when`(humanRepository.saveHuman(humanChange)).thenReturn(humanChange)
        Mockito.doNothing().`when`(eventService).fireHumanEvent(DOMAIN_HUMAN, TYPE_CHANGED, humanChange)

        humanService.changeHuman(humanChange)

        verifyOnce(humanRepository).saveHuman(humanChange)
        verifyOnce(eventService).fireHumanEvent(DOMAIN_HUMAN, TYPE_CHANGED, humanChange)
    }

    @Test
    fun testChangeHuman_HumanDoesNotExists() {
        val human = Human(
            UUID.fromString("25dc70a4-e079-401c-9b20-f5ad18c14fee"),
            "orig",
            "orig"
        )

        Mockito.`when`(humanRepository.findHumanById(human.id)).thenReturn(null)

        assertThrows<HumanNotFoundException>("no exception thrown for unknown human") {
            humanService.changeHuman(human)
        }
    }

    @Test
    fun testCreateHuman() {
        val humanDto = HumanDto(
            "name",
            "test"
        )

        val humanCreate = Human(
            UUID.fromString("ccd80608-d8f5-4fee-8e97-018c392a421a"),
            humanDto.name
        )

        val humanComplete = Human(
            humanCreate.id,
            humanCreate.name,
            humanDto.keycloakName
        )

        Mockito.`when`(humanRepository.findHumanByName(humanDto.name)).thenReturn(null)
        Mockito.`when`(humanFactory.createHuman(humanDto.name)).thenReturn(humanCreate)
        Mockito.`when`(humanRepository.saveHuman(humanComplete)).thenReturn(humanComplete)
        Mockito.doNothing().`when`(eventService).fireHumanEvent(DOMAIN_HUMAN, TYPE_CREATED, humanComplete)

        humanService.createHuman(humanDto)

        verifyOnce(humanRepository).saveHuman(humanComplete)
        verifyOnce(eventService).fireHumanEvent(DOMAIN_HUMAN, TYPE_CREATED, humanComplete)
    }

    @Test
    fun testCreateHuman_HumanAlreadyExists() {
        val humanDto = HumanDto(
            "name",
            "test"
        )

        val human = Human(
            UUID.fromString("0d35db77-e565-446f-95a7-459bdc8a51bb"),
            humanDto.name,
            humanDto.keycloakName
        )

        Mockito.`when`(humanRepository.findHumanByName(humanDto.name)).thenReturn(human)

        assertThrows<HumanAlreadyExistsException>("duplicate creation of human did not throw any exception") {
            humanService.createHuman(humanDto)
        }

        Mockito.verify(humanRepository, Mockito.never()).saveHuman(human)
        Mockito.verify(eventService, Mockito.never()).fireHumanEvent(DOMAIN_HUMAN, TYPE_CREATED, human)
    }
}