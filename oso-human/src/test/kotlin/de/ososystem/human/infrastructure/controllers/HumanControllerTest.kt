package de.ososystem.human.infrastructure.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import de.ososystem.human.domain.dtos.HumanDto
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import de.ososystem.human.domain.entities.Human
import de.ososystem.human.domain.exceptions.HumanAlreadyExistsException
import de.ososystem.human.domain.services.HumanService
import de.ososystem.human.verifyOnce
import org.hamcrest.Matchers.`is`
import org.hamcrest.collection.IsCollectionWithSize
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.nio.charset.StandardCharsets
import java.util.*

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [HumanController::class], secure = false)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class HumanControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var humanService: HumanService

    @Test
    fun testGetAll() {
        val humans = listOf(
            Human(
                UUID.fromString("0798a92a-0a78-43a6-b3e8-ef6c345fd8c3"),
                "bla",
                "blab"
            ),
            Human(
                UUID.fromString("252ebcb1-0295-4a0f-bcac-e6e89a149811"),
                "bli"
            ),
            Human(
                UUID.fromString("926eb761-b56c-4afc-87d9-150c18fd2445"),
                "blup",
                "blupb"
            )
        )

        Mockito.`when`(humanService.getAllHumans()).thenReturn(humans)

        this.mockMvc
            .perform(get("/${HumanController.PATH_HUMAN}").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$", IsCollectionWithSize.hasSize<Collection<Any>>(3)))
            .andExpect(jsonPath("$[0].id", `is`(humans[0].id.toString())))
            .andExpect(jsonPath("$[0].name", `is`(humans[0].name)))
            .andExpect(jsonPath("$[0].keycloakName", `is`(humans[0].keycloakName)))
            .andExpect(jsonPath("$[1].id", `is`(humans[1].id.toString())))
            .andExpect(jsonPath("$[1].name", `is`(humans[1].name)))
            .andExpect(jsonPath("$[1].keycloakName", `is`(humans[1].keycloakName)))
            .andExpect(jsonPath("$[2].id", `is`(humans[2].id.toString())))
            .andExpect(jsonPath("$[2].name", `is`(humans[2].name)))
            .andExpect(jsonPath("$[2].keycloakName", `is`(humans[2].keycloakName)))
            .andDo(document(HumanController.PATH_HUMAN))
    }

    @Test
    fun testGet() {
        val human = Human(
            UUID.fromString("817fe416-68d4-41bd-8f88-dc96aa42bc19"),
            "name",
            "bla"
        )

        Mockito.`when`(humanService.getHuman(human.id)).thenReturn(human)

        this.mockMvc
            .perform(get("/${HumanController.PATH_HUMAN}/${human.id}").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id", `is`(human.id.toString())))
            .andExpect(jsonPath("$.name", `is`(human.name)))
            .andExpect(jsonPath("$.keycloakName", `is`(human.keycloakName)))
            .andDo(document(HumanController.PATH_HUMAN))
    }

    @Test
    fun testGet_HumanDoesNotExist() {
        val human = Human(
            UUID.fromString("817fe416-68d4-41bd-8f88-dc96aa42bc19"),
            "name",
            "bla"
        )

        Mockito.`when`(humanService.getHuman(human.id)).thenReturn(null)

        this.mockMvc
            .perform(get("/${HumanController.PATH_HUMAN}/${human.id}").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound)
    }

    @Test
    fun testCreateHuman() {
        val humanDto = HumanDto(
            "name",
            "key"
        )
        val human = Human(
            UUID.fromString("9ea06590-68a4-45e1-bce2-100b49a7e44b"),
            humanDto.name,
            humanDto.keycloakName
        )

        Mockito.`when`(humanService.createHuman(humanDto)).thenReturn(human)

        this.mockMvc.perform(
                MockMvcRequestBuilders
                    .post("/${HumanController.PATH_HUMAN}")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .content(ObjectMapper().writeValueAsString(humanDto))
            ).andExpect(MockMvcResultMatchers.status().isCreated)
            .andDo(document(HumanController.PATH_HUMAN + "/create"))

        verifyOnce(humanService).createHuman(humanDto)
    }

    @Test
    fun testCreateHuman_throwsHumanAlreadyExistsException() {
        val humanDto = HumanDto(
            "name",
            "key"
        )

        Mockito.`when`(humanService.createHuman(humanDto)).thenThrow(HumanAlreadyExistsException("Human with name<${humanDto.name}> already exists"))

        this.mockMvc.perform(
            MockMvcRequestBuilders
                .post("/${HumanController.PATH_HUMAN}")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(ObjectMapper().writeValueAsString(humanDto))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest)
    }
}